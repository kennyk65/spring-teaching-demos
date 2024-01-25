var result;

// Function to simulate a long-running, blocking process 
// with an inline callback
function longRunningProcess(callback) {
    setTimeout(() => {
        callback("Hello World!");
    }, 2000); // blocking!
}

// Call the function with an inline callback
longRunningProcess((data) => {
    result = data;

    // Do other work...

    // Use results
    console.log("Result from the inline callback:", result);
});
