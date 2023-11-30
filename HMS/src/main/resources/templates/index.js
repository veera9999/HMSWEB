const text = "Your Health, Our Mission!!!";
const typingElement = document.getElementById('dynamic-text');

let i = 0;
let isAdding = true;

function typeText() {
    if (isAdding) {
        // If adding text, add the next character
        typingElement.innerHTML = text.substring(0, i + 1);
        i++;
        if (i > text.length) {
            // Start removing text after a pause
            isAdding = false;
            setTimeout(typeText, 2000); // Wait 2 seconds at the end before deleting
        } else {
            // Continue adding text
            setTimeout(typeText, 300); // Adjust the typing speed here
        }
    } else {
        // If removing text, remove the last character
        typingElement.innerHTML = text.substring(0, i - 1);
        i--;
        if (i === 0) {
            // Start adding text after reaching the beginning
            isAdding = true;
        }
        setTimeout(typeText, 300); // Adjust the deleting speed here
    }
}

typeText(); // Start the typing effect
