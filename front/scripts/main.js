const btn_load = document.getElementById('load');
const btn_start = document.getElementById('start');
const btn_upload = document.getElementById('uploadJSON');

btn_start.addEventListener('click', () => {
    window.location.href = "../pages/menu.html";
});

btn_load.addEventListener('click', () => {
    btn_upload.click();
});

btn_upload.addEventListener('change', async () => {
    const file = btn_upload.files[0];
    const reader = new FileReader();
    reader.onload = async function(e) {
        const data = JSON.parse(e.target.result);

        await fetch("http://localhost:8080/api/todos/upload", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });
    };
    reader.readAsText(file);
});
