
function copyToClipboard() {
    let copyText = document.getElementById("shareUrl");
    copyText.select();
    copyText.setSelectionRange(0, 99999);
    document.execCommand("copy");
    alert("URL copied to clipboard: " + copyText.value);
}