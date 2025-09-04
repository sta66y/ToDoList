const btn_save = document.getElementById('save');
const btn_cancel = document.getElementById('cancel');

const editData = JSON.parse(localStorage.getItem("editTask"));
if (editData) {
    document.getElementById("title").value = editData.task.title;
    document.getElementById("description").value = editData.task.description;
    document.getElementById("status").checked = editData.task.status;
}

btn_cancel.addEventListener('click', () => {
    window,location.href = "../pages/menu.html"
})

btn_save.addEventListener('click', () => {
    const title = document.getElementById("title").value.trim();
    const description = document.getElementById("description").value.trim();
    const status = document.getElementById("status").checked;

    if (title == "") {
        alert("Введите название задачи!");
        return;
    }

    const newTask = {
        "title" : title,
        "description" : description,
        "status" : status
    };

    let tasks = JSON.parse(localStorage.getItem("tasks")) || [];

    if (editData) {
        tasks[editData.index] = newTask;
        localStorage.removeItem("editTask");
    } else {
        tasks.push(newTask);
    }

    localStorage.setItem("tasks", JSON.stringify(tasks));
    window.location.href = "../pages/menu.html";
})