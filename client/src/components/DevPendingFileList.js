import React,{ useState, useEffect , useContext, useRef } from 'react'
import FileContext from '../context/files/FileContext'
import {
    Flex,
    Text,
    UnorderedList,
    ListItem,
    Link,
    createStandaloneToast
} from '@chakra-ui/react'

import { getFileData } from '../service/FileDownloadService'
import { displayWordDoc } from '../service/FileDisplayService'
import FileUpload from './FileUpload'
import { validateFileSize, validateFileType } from '../service/fileValidatorService'
import FileService from '../service/fileService'
import JSZip from 'jszip';
import { convert } from 'docx-to-html';
import word2html from 'html-docx-js';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import Docxtemplater from "docxtemplater";
import * as printJS from "print-js";
// import PizZip from "pizzip";
import { saveAs } from "file-saver";
import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import { PDFDocument } from 'pdf-lib';

const PizZip = require("pizzip");
// import { parseDocx } from 'docx-parser';

// interface Props {
//     fileId: number
// }

function DevPendingFileList(props) {
    const {
        fileId
    } = props
    const ref = useRef(null)
    const refClose = useRef(null)
    const refSubmit = useRef(null)
    //const JSZip = require('jszip');
    //const context = useContext(FileContext);
    //const {Files, getFiles} = context;
    const host = "http://localhost:5000"
    const [fileList, setFileList] = useState([])
    const [efile,setFile] = useState({fileId:-1, fileName: "" , comment:""})
    const [isFileTypesModalOpen, setIsFilesTypeModalOpen] = useState(false)
    const [uploadFormError, setUploadFormError] = useState('')
    const [value,setvalue] = useState('')
    const [documentContent, setDocumentContent] = useState('');
    const [html, setHtml] = useState(null);
    const [Docs,setDocs] = useState([]);

    // const getFiles = async ()=>{
    //     const response = await fetch("http://localhost:5000/api/files/fetchfiles", {
    //         method: 'GET',
    //         headers: {
    //             'Content-Type': 'application/json',
    //           'auth-token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjo3fSwiaWF0IjoxNjc2OTkxNDE3fQ.Bqw-nJePtvpablO8VcDo3qG2vOcT7cNiOysdjWA1fSs'
    //         }
    //       }).then(response => response.json()).then(data => setFileList(data.files));
    //       console.log(fileList[0].fileName)
    //     //   const json = await response.json().then((data) => setFileList(data.files))
    //     //   console.log(json)
          
    // }
    // {
    //     fetch('http://localhost:5000/api/files/fetchfiles', {
    //         method: 'GET',
    //         headers:{
    //             'auth-token': 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjo2fSwiaWF0IjoxNjc2OTg3NDk2fQ.1QQj6pZGyHPDoBQv9fkRKtLfnXEWJZ1EDFum04BVlzA'
    //         }
    //     }).then(response => response.json()).then(data => setFileList(data.files))
    // }
    useEffect(() =>{
        fetch('http://localhost:9191/dev/pending', {
            method: 'GET',
            headers:{
                'Authorization': `Bearer ${localStorage.getItem('token')}` || ''
            }
        }).then(response => {
            //console.log(response)
            return response.json()}).then(data => {
            console.log(data)
            setFileList(data.reverse())
            console.log(fileList)
        })
    }, [])

    const handleFileDownload = async (fileId,fileName) => {
        const fileDownloadResponse = await getFileData(fileId,fileName)

        const toast = createStandaloneToast()
        
        toast({
            title: fileDownloadResponse ? 'Download Successful' : 'Download Failed',
            status: fileDownloadResponse ? 'success' : 'error',
            duration: 3000,
            isClosable: true
        })
    }
    const handleFileView = async (fileName) => {
         const fileDownloadResponse = await displayWordDoc(fileName)
         //const lob = await fileDownloadResponse.blob() 
        //   const pdfUrl = URL.createObjectURL(fileDownloadResponse);
        //   window.open(pdfUrl);
         const reader = new FileReader();
         reader.onload = async function(e) {
            const content = e.target.result;

             
            // // //printJS(pdfUrl)
              //setDocs([{uri: `${pdfUrl}`,fileName: fileName}])
            //  console.log(pdfUrl)
            // console.log(Docs)
            let pdfDoc = await PDFDocument.create();
            let wordDoc = await PDFDocument.load(content);
            let wordDocPages = await pdfDoc.copyPages(wordDoc, wordDoc.getPageIndices());
            wordDocPages.forEach(page => pdfDoc.addPage(page));
            let pdfBytes = await pdfDoc.save();
            const pdfUrl = URL.createObjectURL(new Blob([pdfBytes],{type: 'application/pdf'}));
            console.log(content);
            console.log(pdfBytes);
            console.log(pdfUrl);
            //displayDocument(content);
            //loadDocument(content)

            // const zip = new PizZip(content);
            // const docx = new Docxtemplater(zip,{
            //     paragraphLoop: true,
            //     linebreaks: true,
            // });
            // docx.render();
            // const output = docx.getZip().generate({ type: "blob" });
            

            window.open(pdfUrl);
            //saveAs(output, "example.docx");


            // const wordArray = new Uint8Array(content);
            // const html = convert(content);
            // const doc = new jsPDF();
            
            // doc.html(html, {
            // callback: () => {
            //     doc.save('document.pdf');
            // },
            // x: 10,
            // y: 10,
            // });
            // const pdfBlob = doc.output('blob');
            // const pdfUrl = URL.createObjectURL(pdfBlob);
            // window.open(pdfUrl);
        };
        reader.readAsArrayBuffer(fileDownloadResponse);
        // const toast = createStandaloneToast()
        
        // toast({
        //     title: fileDownloadResponse ? 'Download Successful' : 'Download Failed',
        //     status: fileDownloadResponse ? 'success' : 'error',
        //     duration: 3000,
        //     isClosable: true
        // })
    }
    function displayDocument(content) {
        const documentContainer = document.getElementById('documentContainer');
        documentContainer.innerHTML = `
          <iframe
            width="100%"
            height="600px"
            className="application/pdf"
            sandbox="allow-same-origin allow-scripts allow-popups allow-forms"
            src="${URL.createObjectURL(new Blob([content]),{type: 'application/pdf'})}"
            frameborder="0">
          </iframe>
        `;



        // JSZip.loadAsync(content).then(function(zip) {
        //     const documentXml = zip.file('word/document.xml').async('string');
        //     return Promise.all([documentXml]);
        //   })
        //   .then(function(results) {
        //     const documentXml = results[0];
        //     const parser = new DOMParser();
        //     const _document = parser.parseFromString(documentXml, 'application/xml');
        
        //     // Create a new div element to hold the document contents
        //     const documentDiv = document.createElement('div');
        //     documentDiv.innerHTML = _document.getElementsByTagName('body')[0].innerHTML;
        
        //     // Append the div to the document body
        //     document.body.appendChild(documentDiv);
        //   })
        //   .catch(function(error) {
        //     console.log(error);
        //   });
      }

    if (fileList.length === 0) {
        return null
    }
    
    const deleteFile = async (fileID) => {
        // const response = await fetch('http://localhost:5000/deleteFile', {
        //     method: 'POST',
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     body: JSON.stringify({
        //         fileId
        //     })
        // })
        console.log("Deleting the note with id" + fileId);
        const newFiles = fileList.filter((note)=>{return fileList.fileId!==fileId})
        setFileList(newFiles)
    }

    const handleFileUpdate =async (efileId,efileName,ecomment) => {
        if(ref.current)
        {
            ref.current.click();
            setFile({fileId:efileId,fileName:efileName,comment:ecomment});

        }
        
    }
    const handleClick = (e)=>{ 
        refSubmit.current.click();
        refClose.current.click();
    }

    const onChange = (e) => {
        setFile({...efile, [e.target.name]: e.target.value})
    }
    const handleInputChange = (e) => {
        let inputValue = e.target.value
        setvalue(inputValue)
      }
      const handleSubmit = async (e) =>{
        console.log(e.target[0].value);
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
        console.log(file)
        const fileService = new FileService(file[0],comment)
        const fileUploadResponse = await fileService.uploadFile3(efile.fileId)

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

    

  async function loadDocument(fileName) {


    fetch(`http://localhost:9191/image/${fileName}`)
      .then(response => response.arrayBuffer())
      .then(buffer => {
        JSZip.loadAsync(buffer)
          .then(zip => zip.file('word/document.xml').async('string'))
          .then(documentXml =>  {
            const parser = new DOMParser();
            console.log(documentXml)
            const _document =  parser.parseFromString(documentXml, 'application/xml');
            console.log(_document.documentElement.textContent);
            console.log(_document.getElementsByTagName('w:body')[0].innerHTML);
            const doc = new jsPDF();    
            doc.html(_document.getElementsByTagName('w:body')[0].innerHTML, {
                callback: () => {
                    doc.save('document.pdf');
                    const pdfBlob = doc.output('blob');
                    const pdfUrl = URL.createObjectURL(pdfBlob);
                    window.open(pdfUrl);
                },
                x: 10,
                y: 10,
                });
               
            //setDocumentContent(_document.getElementsByTagName('w:body')[0].innerHTML);
            //setHtml(_document.getElementsByTagName('w:body')[0].innerHTML)
          })
          .catch(error => console.log(error));
      })
      .catch(error => console.log(error));


    // fetch(`http://localhost:9191/image/${fileName}`)
    // .then(response => response.arrayBuffer())
    // .then(buffer => {
    //   convert(buffer).then(html => {
    //     setDocumentContent(html);
    //   });
    // })
    // .catch(error => console.log(error));
    // const html = await convert(fileName);
    // console.log(html);
    // setDocumentContent(html);


    // fetch(`http://localhost:9191/image/${fileName}`)
    // .then(response => response.arrayBuffer())
    // .then(buffer => {
    //   parseDocx(buffer).then(html => {
    //     setDocumentContent(html);
    //   });
    // })
    // .catch(error => console.log(error));
  }

  const renderHTML = () => {
    return { __html: html };
  };

    return (
        <>
        <button ref={ref} type="button" className="btn btn-primary d-none" data-bs-toggle="modal" data-bs-target="#exampleModal">
                Launch demo modal
        </button>
            <div className="modal fade" id="exampleModal" tabIndex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="exampleModalLabel">Send Review</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <form className="my-3">
                                <div className="mb-3">
                                    <label htmlFor="title" className="form-label">File Name</label>
                                    <input type="text" disabled={true} className="form-control" id="etitle" name="etitle" value={efile.fileName} aria-describedby="emailHelp" onChange={onChange} minLength={5} required/>
                                </div>
                                <div className="mb-3">
                                    <label htmlFor="description" className="form-label">Comment</label>
                                    <input type="text" disabled={true} className="form-control" id="edescription" name="edescription" value={efile.comment} onChange={onChange} minLength={5} required/>
                                </div>
                            </form>
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
                                <button ref={refSubmit} type="submit" className="btn btn-primary mb-3 d-none">Submit</button>
                            </form>
                        </div>
                        <div className="modal-footer">
                            <button ref={refClose} type="button" className="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button disabled={efile.comment.length<5} onClick={handleClick} type="button" className="btn btn-primary">Update Note</button>
                        </div>
                    </div>
                </div>
            </div>
            {html && (
        <div
          dangerouslySetInnerHTML={renderHTML()}
          style={{ margin: '20px', border: '1px solid black', padding: '10px' }}
        />
      )}
            {<div dangerouslySetInnerHTML={{ __html: documentContent }}></div>}
            {Docs.length>0 && <DocViewer documents={Docs} className="doc-viewer" config={{
            header: {
              disableHeader: false,
              disableFileName: false,
              retainURLParams: false,
            },
          }} pluginRenderers={DocViewerRenderers} />}
        <div className='row my-3'>
            
            {fileList.map(({ id, name, comment, reviewer_id }) => (
                        <div className="col-md-4" key={id}>
                            <div className="card my-3 text-center d-flex">
                            <div className="card-header">
                                File : {name}
                            </div>
                            <div className="card-body">
                                <h5 className="card-title"><b>Comment:</b></h5>
                                <p className="card-text my-3">{comment}</p>
                                <Link to="/" onClick={() => handleFileDownload(id,name)} className="btn btn-outline-primary mx-3">Download File</Link>
                                <Link to="/" onClick={() => handleFileUpdate(id,name,comment)} className="btn btn-outline-primary d-none">Review</Link>
                                <Link to="/" onClick={() => handleFileView(name)} className="btn btn-outline-primary">View File</Link>
                                {/* <Link to="/" onClick={() => loadDocument(name)} className="btn btn-outline-primary">View File</Link> */}
                            </div>
                            <div className="card-footer text-muted">
                                Assigned to {reviewer_id}
                            </div>
                        </div>
                        </div> 
                    
                ))}

                
        </div>

        <div id="documentContainer"></div>
        {/* <button onClick={loadDocument}>Load Document</button> */}
      
        </>    
    )
}

export default DevPendingFileList