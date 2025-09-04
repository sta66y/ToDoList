const btn_add = document.getElementById('add');
const btn_back = document.getElementById('back');
const btn_save = document.getElementById('save');
const btn_saveJSON = document.getElementById('saveJSON');

btn_add.addEventListener('click', () => {
    window,location.href = "../pages/addNew.html"
})

let tasks = JSON.parse(localStorage.getItem("tasks")) || [];
let taskList = document.getElementById("taskList");

renderTasks();

function renderTasks() {
    taskList.innerHTML = "";

    tasks.forEach((task, index) => {
    let li = document.createElement("li");
    
    let span = document.createElement("span");
    span.textContent = task.title;

    let buttons = document.createElement("div");
    buttons.className = "buttons";

    let btn_edit = document.createElement("button");
    btn_edit.textContent = "E";

    let btn_delete = document.createElement("button");
    btn_delete.textContent = "D";

    let checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.status;

    buttons.appendChild(btn_edit);
    buttons.appendChild(btn_delete);
    buttons.appendChild(checkbox);
    li.appendChild(span);
    li.appendChild(buttons);

    li.className = "task";

    taskList.appendChild(li);

    btn_delete.addEventListener('click', () => {
        tasks.splice(index, 1);
        localStorage.setItem("tasks", JSON.stringify(tasks));
        renderTasks();
    })

    checkbox.addEventListener('click', () => {
        task.status = checkbox.checked;
        localStorage.setItem("tasks", JSON.stringify(tasks));
        renderTasks();
    })

    btn_edit.addEventListener('click', () => {
        localStorage.setItem("editTask", JSON.stringify({ task, index }));
        window.location.href = "addNew.html";
    })
});

}

btn_back.addEventListener('click', () => {
    localStorage.clear();
    window.location.href = "../pages/index.html";
})

btn_save.addEventListener('click', () => {
    window.location.href = "../pages/index.html";
})

btn_saveJSON.addEventListener('click', () => {
    tasks = localStorage.getItem("tasks") || [];
    const blob = new Blob([tasks], { type: "application/json" });
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "tasks.json";
    a.click();
    URL.revokeObjectURL(a.href);
})

