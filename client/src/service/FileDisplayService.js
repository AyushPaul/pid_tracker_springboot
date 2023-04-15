
//const Office = require("@microsoft/office-js");

//import Office from "@types/office-js";


async function displayWordDoc(fileName) {
    // Create a new FileReader object
    const response = await fetch(`http://localhost:9191/image/${fileName}`)
    console.log(response)
    const lob = await response.blob()
    return lob;
    // var reader = new FileReader();
  
    // // Define a function to be called when the FileReader has finished reading the blob
    // reader.onloadend = function () {
    //   // Use the Office.js library to display the Word Document in the browser
    //   Office.context.document.setSelectedDataAsync(reader.result, { coercionType: "html" });
      
    // };
  
    // // Read the blob as a data URL
    // reader.readAsDataURL(lob);
  }

  export {
    displayWordDoc
}

