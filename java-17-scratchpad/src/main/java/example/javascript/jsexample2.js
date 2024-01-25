(function() {
    setTimeout(function doThing1() {
        const result1 = "Hello";

        setTimeout(function doThing2() {
            const result2 = result1 + " World";

            setTimeout(function doThing3() {
                const result3 = result2 + "!!";

                console.log("Result from JavaScript:", result3);
            }, 1000);
        }, 1000);
    }, 1000);
})();
