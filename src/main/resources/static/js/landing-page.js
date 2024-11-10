// Toggle Card Views
document.querySelector('.register-btn').addEventListener('click', function () {
    document.querySelector('.card').classList.toggle('flipped');
});

document.querySelector('.login-btn').addEventListener('click', function () {
    document.querySelector('.card').classList.toggle('flipped');
});

// Function to show popup
function showPopup(message) {
    const popup = document.getElementById('errorPopup');
    popup.innerText = message;
    popup.style.display = 'block';
    setTimeout(() => {
        popup.style.display = 'none';
    }, 2000); // Hide after 2 seconds
}

// URL Parameter Based Popups
const params = new URLSearchParams(window.location.search);
if (params.has('error') && params.get('error') === 'true') {
    showPopup('Username or Password not found');
}
if (params.has('logout')) {
    showPopup('Logged out successfully');
}
if (params.has('success')) {
    const successMessage = params.get('success') === 'true' ? 'Registered successfully' : 'Account already exists';
    showPopup(successMessage);
}

// Reusable Function to Show Error Message
function showErrorMessage(inputElement, message) {
    let errorMessageElement = inputElement.nextElementSibling;
    if (errorMessageElement && errorMessageElement.classList.contains('error-message')) {
        errorMessageElement.innerHTML = message;
    } else {
        errorMessageElement = document.createElement('div');
        errorMessageElement.classList.add('error-message');
        errorMessageElement.innerHTML = message;
        inputElement.parentNode.insertBefore(errorMessageElement, inputElement.nextSibling);
    }
    inputElement.classList.add('error');
}

// Function to Clear Error Message
function clearErrorMessage(inputElement) {
    const errorMessageElement = inputElement.nextElementSibling;
    if (errorMessageElement && errorMessageElement.classList.contains('error-message')) {
        errorMessageElement.remove();
    }
    inputElement.classList.remove('error');
}

// Validation Functions (Reusable)
function validateUsername(username) {
    if (username.length < 3) {
        return 'Username must be at least 3 characters long';
    }
    return true;
}

function validateEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!emailPattern.test(email)) {
        return 'Please enter a valid email address';
    }
    return true;
}

function validatePassword(password) {
    const minLength = 8;
    const upperCasePattern = /[A-Z]/;
    const lowerCasePattern = /[a-z]/;
    const numberPattern = /[0-9]/;
    const specialCharPattern = /[!@#$%^&*(),.?":{}|<>]/;

    if (password.length < minLength) {
        return 'Password must be at least 8 characters long';
    }
    if (!upperCasePattern.test(password)) {
        return 'Password must contain at least one uppercase letter';
    }
    if (!lowerCasePattern.test(password)) {
        return 'Password must contain at least one lowercase letter';
    }
    if (!numberPattern.test(password)) {
        return 'Password must contain at least one number';
    }
    if (!specialCharPattern.test(password)) {
        return 'Password must contain at least one special character';
    }
    return true;
}

// Form Real-Time Validation
document.getElementById('register-form').addEventListener('input', function () {
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;

    // Validate each field and show or clear error messages
    handleValidation('register-username', validateUsername(username));
    handleValidation('register-email', validateEmail(email));
    handleValidation('register-password', validatePassword(password));
});

// General Form Field Validation Handler
function handleValidation(inputId, validationResult) {
    const inputElement = document.getElementById(inputId);
    if (validationResult === true) {
        clearErrorMessage(inputElement);
    } else {
        showErrorMessage(inputElement, validationResult);
    }
}

// Form Submit Validation
document.getElementById('register-form').addEventListener('submit', function (event) {
    let isValid = true;

    // Validate all fields and show error messages
    const username = document.getElementById('register-username').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;

    if (validateUsername(username) !== true) {
        showErrorMessage(document.getElementById('register-username'), validateUsername(username));
        isValid = false;
    }
    if (validateEmail(email) !== true) {
        showErrorMessage(document.getElementById('register-email'), validateEmail(email));
        isValid = false;
    }
    if (validatePassword(password) !== true) {
        showErrorMessage(document.getElementById('register-password'), validatePassword(password));
        isValid = false;
    }

    // Prevent form submission if validation fails
    if (!isValid) {
        event.preventDefault();
    }
});
