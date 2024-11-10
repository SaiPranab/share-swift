document.getElementById('file-upload').addEventListener('change', function(event) {
    const fileNames = Array.from(event.target.files).map(file => file.name);
    document.getElementById('file-name').textContent = fileNames.join(', ');
});

// Drag and drop functionality
const dragDropArea = document.getElementById('dragDropArea');
const fileInput = document.getElementById('file-upload');

dragDropArea.addEventListener('dragover', (event) => {
    event.preventDefault();
    dragDropArea.classList.add('border-info');
});

dragDropArea.addEventListener('dragleave', () => {
    dragDropArea.classList.remove('border-info');
});

dragDropArea.addEventListener('drop', (event) => {
    event.preventDefault();
    dragDropArea.classList.remove('border-info');
    const files = event.dataTransfer.files;
    fileInput.files = files;
    document.getElementById('file-name').textContent = files[0].name;
});

// -----------------------------------------------------------------------
document.getElementById('uploadForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the form from submitting normally

    // Get the files and uploadedBy fields
    let fileInput = document.getElementById('file-upload');
    let uploadedBy = document.getElementById('uploadedBy').value;
    let formData = new FormData();
    
    // Add files and uploadedBy to the FormData
    Array.from(fileInput.files).forEach(file => {
        formData.append("files", file);
    });
    formData.append("uploadedBy", uploadedBy);

    // Create the AJAX request
    let xhr = new XMLHttpRequest();
    xhr.open('POST', '/files/upload', true);

    // Show the loading overlay when upload starts
    document.getElementById('loadingOverlay').style.display = 'flex';

    // Set up the progress event listener
    xhr.upload.addEventListener('progress', function(e) {
        if (e.lengthComputable) {
            // Calculate the progress percentage
            let percent = (e.loaded / e.total) * 100;
            // Update the circular progress
            let circle = document.getElementById('progress-circle');
            let offset = 283 - (percent / 100) * 283; // 283 is the circumference
            circle.style.strokeDashoffset = offset;

            // Update the percentage text inside the circle
            let progressText = document.getElementById('progress-text');
            progressText.innerText = Math.round(percent) + '%';
        }
    });

    // When upload is complete
    xhr.onload = function() {
        if (xhr.status == 200) {
            // alert("File uploaded successfully!");
        } else {
            alert("File upload failed!");
        }
        
        // Hide the loading overlay after upload
        document.getElementById('loadingOverlay').style.display = 'none';
        
        location.reload();
    };

    // Error handling
    xhr.onerror = function() {
        alert("An error occurred during the upload.");
        document.getElementById('loadingOverlay').style.display = 'none';
    };

    // Cancel upload on cross click
    document.getElementById('closeUpload').addEventListener('click', function() {
        xhr.abort(); // Abort the request to cancel the upload
        document.getElementById('loadingOverlay').style.display = 'none'; // Hide the overlay
        alert("File upload canceled!"); // Optionally alert the user
    });

    // Send the form data
    xhr.send(formData);
});