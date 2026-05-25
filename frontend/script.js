const BASE_URL = "http://localhost:8080";

/* ---------------- IMAGE PREVIEW ---------------- */

document.getElementById("encodeImage")
    .addEventListener("change", function (event) {

        const file = event.target.files[0];

        if (file) {
            document.getElementById("encodePreview").src =
                URL.createObjectURL(file);
        }
    });

document.getElementById("decodeImage")
    .addEventListener("change", function (event) {

        const file = event.target.files[0];

        if (file) {
            document.getElementById("decodePreview").src =
                URL.createObjectURL(file);
        }
    });

/* ---------------- CHARACTER COUNTER ---------------- */

const textarea = document.getElementById("secretMessage");

textarea.addEventListener("input", () => {

    document.getElementById("charCount").innerText =
        textarea.value.length + " characters";
});

/* ---------------- DARK MODE ---------------- */

function toggleTheme() {
    document.body.classList.toggle("dark-mode");
}

/* ---------------- ENCODE MESSAGE ---------------- */

async function encodeMessage() {

    const image = document.getElementById("encodeImage").files[0];

    const message =
        document.getElementById("secretMessage").value;

    if (!image || !message) {

        alert("Please select an image and enter a secret message.");

        return;
    }

    const formData = new FormData();

    formData.append("image", image);

    formData.append("message", message);

    try {

        const response = await fetch(`${BASE_URL}/encode`, {

            method: "POST",

            body: formData
        });

        if (!response.ok) {

            throw new Error("Encoding failed");
        }

        const blob = await response.blob();

        const downloadUrl =
            window.URL.createObjectURL(blob);

        const link = document.createElement("a");

        link.href = downloadUrl;

        link.download = "encoded-image.png";

        document.body.appendChild(link);

        link.click();

        link.remove();

        alert(
            "✅ Secret message encoded successfully!\nImage downloaded."
        );

        /* CLEAR INPUTS */

        document.getElementById("encodeImage").value = "";

        document.getElementById("secretMessage").value = "";

        document.getElementById("encodePreview").src = "";

        document.getElementById("charCount").innerText =
            "0 characters";

    } catch (error) {

        console.error(error);

        alert("❌ Error while encoding image.");
    }
}

/* ---------------- DECODE MESSAGE ---------------- */

async function decodeMessage() {

    const image =
        document.getElementById("decodeImage").files[0];

    if (!image) {

        alert("Please select an encoded image.");

        return;
    }

    const formData = new FormData();

    formData.append("image", image);

    try {

        const response = await fetch(`${BASE_URL}/decode`, {

            method: "POST",

            body: formData
        });

        const text = await response.text();

        document.getElementById("decodedText").innerHTML =
            `✅ Hidden Message:<br><br>${text}`;

        alert("✅ Message decoded successfully!");

        /* CLEAR INPUT */

        document.getElementById("decodeImage").value = "";

        document.getElementById("decodePreview").src = "";

    } catch (error) {

        console.error(error);

        alert("❌ Error while decoding image.");
    }
}