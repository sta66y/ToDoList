const btn_load = document.getElementById('load');
const btn_start = document.getElementById('start');
const btn_upload = document.getElementById('uploadJSON');

btn_start.addEventListener('click', () => {
    window.location.href = "../pages/menu.html";
})

btn_load.addEventListener('click', () => {
    btn_upload.click();
})

btn_upload.addEventListener('change', () => {
    const file = btn_upload.files[0];

    const reader = new FileReader();
    reader.onload = function(e) {
        const data = JSON.parse(e.target.result);
        localStorage.setItem("tasks", JSON.stringify(data));
  };
    reader.readAsText(file);
});
