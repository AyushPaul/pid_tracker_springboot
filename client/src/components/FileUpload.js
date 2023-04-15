import { SyntheticEvent, useState, Dispatch, SetStateAction,useRef } from 'react'
import {
    Box,
    Text,
    Flex,
    Button,
    Input,
    createStandaloneToast
} from '@chakra-ui/react'

import {
    FormControl,
    FormLabel,
    FormErrorMessage,
    FormHelperText,
  } from '@chakra-ui/react'
  import { Textarea } from '@chakra-ui/react'

import AcceptedFileTypesModal from './AcceptedFileTypesModal'
import { validateFileSize, validateFileType } from '../service/fileValidatorService'
import FileService from '../service/fileService'
const Office = window.Office;
const Word = window.Word;

const JSZip = require('jszip');
//import { Form } from "react-final-form"
// interface Props {
//     setFileId: Dispatch<SetStateAction<number>>
// }



function FileUpload(props) {
    const {
        setFileId
    } = props
    const [isFileTypesModalOpen, setIsFilesTypeModalOpen] = useState(false)
    const [uploadFormError, setUploadFormError] = useState('')
    const [value,setvalue] = useState('')
    const ref = useRef(null)

    const handleInputChange = (e) => {
        let inputValue = e.target.value
        setvalue(inputValue)
      }
      const handleSubmit = async (e) =>{
        console.log(e.target[0].value);
        const file = e.target[1].files[0];
        console.log("File :")
        console.log(file)
  const reader = new FileReader();
  reader.onload = function(e) {
    const content = e.target.result;
    console.log(content);
    displayDocument(content);
  };
  reader.readAsArrayBuffer(file);
        handleFileUpload(e.target[1],e.target[0].value)
        e.preventDefault();
        
      }
    const handleFileUpload = async (element,comment) => {
        //element.preventDefault()
        console.log(element)
        const file = element.files
        
        if (!file) {
            return
        }
        
        const validFileSize = await validateFileSize(file[0].size)
        const validFileType = await validateFileType(FileService.getFileExtension(file[0].name))

        if (!validFileSize.isValid) {
            setUploadFormError(validFileSize.errorMessage)
            return
        }

        if (!validFileType.isValid) {
            setUploadFormError(validFileType.errorMessage)
            return
        }
        
        if (uploadFormError && validFileSize.isValid) {
            setUploadFormError('')
        }

        const fileService = new FileService(file[0],comment)
        const fileUploadResponse = await fileService.uploadFile2()

        element.value = ''

        const toast = createStandaloneToast()

        toast({
            title: fileUploadResponse.success ? 'File Uploaded' : 'Upload Failed',
            description: fileUploadResponse.message,
            status: fileUploadResponse.success ? 'success' : 'error',
            duration: 3000,
            isClosable: true
        })
        //setFileId(fileUploadResponse.fileId ?? 0)
    }

    function displayDocument(content) {
        // const documentContainer = document.getElementById('documentContainer');
        // documentContainer.innerHTML = `
        //   <iframe
        //     width="100%"
        //     height="600px"
        //     src="https://view.officeapps.live.com/op/embed.aspx?src=https://file-examples-com.github.io/uploads/2017/02/file-sample_100kB.doc"
        //     frameborder="0">
        //   </iframe>
        // `;

        // Word.run(function(context) {
        //     const newDocument = context.application.createDocument();
        //     const body = newDocument.body;
        //     const range = body.insertFileFromBase64(content, 'replace');
        //     range.select();
        //     return context.sync().then(function() {
        //       const documentContainer = document.getElementById('documentContainer');
        //       documentContainer.innerHTML = '';
        //       documentContainer.appendChild(newDocument.getHtml());
        //     });
        //   })
        //   .catch(function(error) {
        //     console.log(error);
        //   });

        JSZip.loadAsync(content).then(function(zip) {
            const documentXml = zip.file('word/document.xml').async('string');
            return Promise.all([documentXml]);
          })
          .then(function(results) {
            const documentXml = results[0];
            console.log(documentXml);
            const parser = new DOMParser();
            const _document = parser.parseFromString(documentXml, 'application/xml');
            console.log(_document);
            const documentContainer = ref
            documentContainer.innerHTML = _document.getElementsByTagName('body')[0].innerHTML;
          })
          .catch(function(error) {
            console.log(error);
          });
      }

    return (
        <>
        <Box
            width="50%"
            m="100px auto"
            padding="2"
            shadow="base"
        >
            <Flex
                direction="column"
                alignItems="center"
                mb="5"
            >
                {/* <Text fontSize="2xl" mb="4">Upload a Document</Text> */}
                {/* <Button
                    size="sm"
                    colorScheme="green"
                    onClick={() => setIsFilesTypeModalOpen(true)}
                >
                    Accepted File Types
                </Button> */}
                {
                    uploadFormError &&
                    <Text mt="5" color="red">{uploadFormError}</Text>
                }
                <Box
                    mt="10"
                    ml="24"
                >
                    <form  onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="exampleFormControlTextarea1" className="form-label">Add Comments</label>
                            <textarea className="form-control" id="exampleFormControlTextarea1" onChange={handleInputChange}
                            placeholder='Add Comments'></textarea>
                        </div>
                        {/* <FormLabel>Add File</FormLabel> */}
                        {/* <Input
                            type="file"
                            variant="unstyled"
                            //onChange={(e: SyntheticEvent) => handleFileUpload(e.currentTarget as HTMLInputElement)}
                        /> */}
                        <div className="mb-3">
                            <label htmlFor="formFile" className="form-label">Add File</label>
-                            <input className="form-control" type="file" id="formFile" 
                                    //onChange={(e) => handleFileUpload(e.currentTarget)}
                                    />
                        </div>
                        <button type="submit" className="btn btn-primary mb-3">Submit</button>
                    </form>
                    
                </Box>
            </Flex>
            <AcceptedFileTypesModal 
                isOpen={isFileTypesModalOpen}
                onClose={() => setIsFilesTypeModalOpen(false)}
            />
        </Box>

        <div id="documentContainer" ref={ref}></div>
        </>
    )
}

export default FileUpload