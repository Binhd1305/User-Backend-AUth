function validatePassword() {
    const password = document.getElementById("password").value;
    const confirm = document.getElementById("confirm").value;

    if (password !== confirm) {
        alert("Passwords do not match!");
        return false; // prevent form submission
    }
    return true; // allow form submission
}

