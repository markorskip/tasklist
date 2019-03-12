new Vue({
el: '#app',
data: {
test: "testing vue",
debug: true,
tasklists: null,
username: th_username,
},
mounted() {
this.refreshTaskList();
},
methods: {
newTaskList: function() {
axios.get("/api/users/" + th_username + "/tasklists/new")
.then((response) => {
console.log(response.data);
this.refreshTaskList();
}, (error) => {
console.log("ERROR")
})
},
    refreshTaskList: function () {
        axios.get("/api/users/" + th_username + "/tasklists/")
            .then((response) => {
                console.log(response.data);
                this.tasklists = response.data._embedded.tasklists;
            }, (error) => {
                console.log(error)
            })

    },
    deleteTaskList: function (tasklist) {
        console.log(tasklist);
        axios.delete("/api/users/" + th_username + "/tasklists/del/" + tasklist.id)
            .then((response) => {
                console.log(tasklist)
                console.log(response);
                this.refreshTaskList();
            }, (error) => {
                console.log(tasklist);
                console.log(error)
            })
    },
    toggleEdit: function(tasklist) {
        this.edit = !this.edit;
    }
}
});
