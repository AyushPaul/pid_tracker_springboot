async function getFileData(fileId: number,fileName:string): Promise<boolean> {
    //const fileResponse = await fetch(`http://localhost:5000/downloadFile/${fileId}`)
    const response = await fetch(`http://localhost:9191/image/${fileName}`)
    console.log(response)
    const lob = await response.blob()
    downloadFile(fileName, lob)
    // if (response.headers.get('content-type') === 'application/octet-stream') {
    //     //const blob = await fileResponse.blob()
    //     const fileName = getFileNameFromContentDisposition(response.headers)
        
    //     if (fileName === '') {
    //         return false
    //     }

        

    //     return true
    // }

    return true
}

function getFileNameFromContentDisposition(headers: Headers): string {
    return headers.get('Content-disposition')?.split('filename=')[1].replace('"', '') ?? ''
}

function downloadFile(fileName: string, blob: Blob) {
    const url = window.URL.createObjectURL(blob)

    // if (navigator.appVersion.indexOf('Trident/') !== -1) {
    //     navigator.msSaveOrOpenBlob(blob, fileName)
    //     return
    // }

    const element = document.createElement('a')
    element.href = url
    element.download = fileName
    element.click()
}



export {
    getFileData
}