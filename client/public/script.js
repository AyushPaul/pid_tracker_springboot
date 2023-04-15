
const fileInput = document.getElementById('fileInput');
fileInput.addEventListener('change', handleFileSelect, false);

function handleFileSelect(event) {
  const file = event.target.files[0];
  const reader = new FileReader();
  reader.onload = function(event) {
    const content = event.target.result;
    displayDocument(content);
  };
  reader.readAsArrayBuffer(file);
}



function displayDocument(content) {
    JSZip.loadAsync(content).then(function(zip) {
      const documentXml = zip.file('word/document.xml').async('string');
      return Promise.all([documentXml]);
    })
    .then(function(results) {
      const documentXml = results[0];
      const parser = new DOMParser();
      const document = parser.parseFromString(documentXml, 'application/xml');
      const documentContainer = document.getElementById('documentContainer');
      documentContainer.innerHTML = document.getElementsByTagName('body')[0].innerHTML;
    })
    .catch(function(error) {
      console.log(error);
    });
  }