// ----------------- Sign Up -----------------
document.addEventListener("DOMContentLoaded", () => {

    const signupForm = document.getElementById("signupForm");
    if (signupForm) {
        signupForm.addEventListener("submit", async function(e) {
            e.preventDefault();

            const firstname = document.getElementById("firstname").value.trim();
            const lastname = document.getElementById("lastname").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("password").value.trim();
            const confirm = document.getElementById("confirm").value.trim();

            if (password !== confirm) {
                alert("Passwords do not match!");
                return;
            }

            const data = { firstname, lastname, email, password };

            try {
                const response = await fetch("http://localhost:8080/api/v1/auth/register", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const result = await response.json();
                    localStorage.setItem("jwtToken", result.token);
                    window.location.href = "thebistro.html";
                } else {
                    const error = await response.text();
                    alert("Sign Up Error: " + error);
                }
            } catch (err) {
                console.error(err);
                alert("Sign Up failed.");
            }
        });
    }

    // ----------------- Sign In -----------------
    const signinForm = document.getElementById("signinForm");
    if (signinForm) {
        signinForm.addEventListener("submit", async function(e) {
            e.preventDefault();

            const email = document.getElementById("usernameLogin").value.trim();
            const password = document.getElementById("passwordLogin").value.trim();
            const data = { email, password };

            try {
                const response = await fetch("http://localhost:8080/api/v1/auth/authenticate", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data)
                });

                const messageEl = document.getElementById("message");

                if (response.ok) {
                    const result = await response.json();
                    localStorage.setItem("jwtToken", result.token);
                    messageEl.style.color = "green";
                    messageEl.textContent = "Sign In successful! Redirecting...";
                    setTimeout(() => window.location.href = "thebistro.html", 1000);

                } else {
                    const error = await response.text();
                    messageEl.style.color = "red";
                    messageEl.textContent = "Login failed: " + error;
                }

            } catch (err) {
                console.error(err);
                messageEl.textContent = "Login failed. Check console.";
            }
        });
    }
});
