import DocViewer, { DocViewerRenderers } from "@cyntler/react-doc-viewer";
import React,{ useState, useEffect , useContext, useRef } from 'react'

const DocViewerWithInputApp = () => {
    const [selectedDocs, setSelectedDocs] = useState([]);
  
    return (
      <>
        <input
          type="file"
          accept=".pdf"
          multiple
          onChange={(el) =>
            el.target.files?.length &&
            setSelectedDocs(Array.from(el.target.files))
          }
        />
        {selectedDocs.length>0 && <DocViewer
          documents={selectedDocs.map((file) => ({
            uri: window.URL.createObjectURL(file),
            fileName: file.name,
          }))}
          pluginRenderers={DocViewerRenderers}
        />}
      </>
    );
  };

  export default DocViewerWithInputApp