
function Task(label, depth) {
    this.label = label;
    this.depth = depth;
    this.children = false;
    this.edit=false;
    this.estimatedTime=0;
    this.estimatedCost=0;
    this.time=0;
    this.cost=0;
    this.complete = false;
    this.createdDate = new Date().toDateString();
    this.description = function() {
        return this.label + " "  + this.depth;
    }
}

Vue.component('task', {
    template: '#task',
    props: ['task','index'],
    computed: {
        indent() {
            return { transform: `translate(${this.task.depth * 30}px)` }
        },
        description() { // Used for more details
            return this.task.label + " " + this.estimatedTime
        }
    }
});

init = [new Task("All Tasks",0)];

function reindex(tasks) {
    console.log("Reindex triggered");

    function setParent(task){
        task.children=true;
        console.log("index of parent:" + tasks.indexOf(task));

        // figure out who my children are and add up the cost
        let childrenArr = [];
        let computedCost = 0;
        let computedTime = 0;
        let completedCost = 0;
        let completedTime = 0;
        let remainingCost = 0;
        let remainingTime = 0;
        for (var i = tasks.indexOf(task)+1; i < tasks.length; i++) {
            if (tasks[i].depth == task.depth+1) {
                childrenArr.push(tasks[i]);
                computedCost += parseFloat(tasks[i].cost);
                computedTime += parseFloat(tasks[i].time);
                if (tasks[i].complete) {
                    completedCost += parseFloat(tasks[i].completedCost);
                    completedTime += parseFloat(tasks[i].time);
                }

            } else if (tasks[i].depth > task.depth+1) {
                // Grandchild, do nothing
            } else if (tasks[i].depth < task.depth+1) {
                //Another parent of sibling, break out of the loop
                break;
            }
        }

        //if the computed cost is greater then 0, alter the cost to equal that, otherwise use the estimated cost
        if(computedCost > 0) { task.cost = computedCost; } else {task.cost = task.estimatedCost; }
        if(computedTime > 0) { task.time = computedTime; } else {task.time = task.estimatedTime; }

        return task;
    }

    function setChild(task) {
        task.children=false;
        task.cost = task.estimatedCost;
        task.time = task.estimatedTime;
        return task;
    }

    // Loop through every task backwards so we start with children first
    for (var i = tasks.length-1; i >=0; i--) {
        if (tasks[i+1]!==undefined) {
            if (tasks[i+1].depth> tasks[i].depth) {
                //parent logic
                console.log("Parent")
                tasks[i] = setParent(tasks[i]);
            } else { tasks[i] = setChild(tasks[i]); }

        } else { tasks[i] = setChild(tasks[i]) };  // This executes only once

    } // End of for loop
    return tasks;
}


new Vue({
    el: '#app',
    data: {
        tasks: init,
        name: null,
        debug: true,
        editName: false
    },
    mounted() {
        this.refresh();
    },
    methods: {
        addRoot() {
            newTask = new Task('',0);
            newTask.edit=true;
            this.tasks.splice(0,0,newTask)
        },
        addChild(parentIndexArray) {
            parentTask = parentIndexArray[0];
            index= parentIndexArray[1];
            newTask = new Task('',parentTask.depth+1);
            newTask.edit=true;
            this.tasks.splice(index+1,0,newTask);
            this.tasks = reindex(this.tasks);
        },
        deleteTask(taskToDeleteArray) {
            taskToDelete = taskToDeleteArray[0];
            index = taskToDeleteArray[1];
            console.log(index);
            this.tasks.splice(index,1);
            this.tasks = reindex(this.tasks);
        },
        toggleEdit(index) {
            this.tasks[index].edit = !this.tasks[index].edit;
            this.tasks = reindex(this.tasks);
        },
        toggleEditName() {
            this.editName = !this.editName;
        },
        refresh() {
            axios.get("/api/tasklists/" + tasklist_id)
                .then((response) =>
                {
                    // console.log(response.data);
                    // console.log(response.data.taskListName);
                    // console.log(response.data.jsonTaskList);
                    this.tasks = JSON.parse(response.data.jsonTaskList);
                    //this.tasks = response.data.jsonTaskList;
                    this.name = response.data.taskListName;
                }, (error) => {
                    console.log("ERROR")
                })
        },
        save() {
            axios({
                method: 'patch',
                headers: {
                    'content-type':'application/json'
                },
                url: "/api/tasklists/" + tasklist_id ,
                data: {
                    jsonTaskList : JSON.stringify(this.tasks),
                    taskListName : this.name
                }
            })
                .then(r => console.log(r.status))
                .catch(e =>
                        console.log(e),
                    console.log(this.tasks)

                );
        }
    }
});


