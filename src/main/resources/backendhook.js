// Wait until the entire HTML document is fully loaded
document.addEventListener("DOMContentLoaded", () => {

    // ================= SIGN UP =================
    // Get the sign-up form element by ID
    const signupForm = document.getElementById("signupForm");

    // Only run this code if the sign-up form exists on the page
    if (signupForm) {
        signupForm.addEventListener("submit", async function(e) {
            // Prevent the default form submission behavior
            e.preventDefault();

            // Retrieve and trim user input values
            const firstname = document.getElementById("firstname").value.trim();
            const lastname = document.getElementById("lastname").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();
            const confirm = document.getElementById("confirm").value.trim();

            // Validate password confirmation
            if (password !== confirm) {
                alert("Passwords do not match!");
                return;
            }

            // Create data object to send to backend API
            const data = { firstname, lastname, email, password };

            try {
                // Send POST request to register endpoint
                const response = await fetch("http://localhost:8080/api/v1/auth/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                // If registration is successful
                if (response.ok) {
                    const result = await response.json();

                    // Store JWT token in local storage
                    localStorage.setItem("jwtToken", result.token);

                    // Redirect user to main page
                    window.location.href = "thebistro.html";
                } else {
                    // Display backend error message
                    const error = await response.text();
                    alert("Sign Up Error: " + error);
                }
            } catch (err) {
                // Handle network or unexpected errors
                console.error(err);
                alert("Sign Up failed.");
            }
        });
    }

    // ================= SIGN IN =================
    // Get the sign-in form element by ID
    const signinForm = document.getElementById("signinForm");

    // Only run this code if the sign-in form exists on the page
    if (signinForm) {
        signinForm.addEventListener("submit", async function(e) {
            // Prevent page reload
            e.preventDefault();

            // Retrieve login credentials
            const email = document.getElementById("usernameLogin").value.trim();
            const password = document.getElementById("passwordLogin").value.trim();

            // Create request body
            const data = { email, password };

            try {
                // Send POST request to authentication endpoint
                const response = await fetch("http://localhost:8080/api/v1/auth/authenticate", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                // Message element for user feedback
                const messageEl = document.getElementById("message");

                if (response.ok) {
                    const result = await response.json();

                    // Save JWT token to local storage
                    localStorage.setItem("jwtToken", result.token);

                    // Display success message
                    messageEl.style.color = "green";
                    messageEl.textContent = "Sign In successful! Redirecting...";

                    // Redirect after short delay
                    setTimeout(() => window.location.href = "thebistro.html", 1000);
                } else {
                    // Display error message from server
                    const error = await response.text();
                    messageEl.style.color = "red";
                    messageEl.textContent = "Login failed: " + error;
                }
            } catch (err) {
                // Handle unexpected errors
                console.error(err);
                messageEl.textContent = "Login failed. Check console.";
            }
        });
    }
});
