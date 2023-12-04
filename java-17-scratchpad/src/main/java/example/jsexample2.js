(function() {
    (function doThing1() {
        console.log("Thing 1 done");
        const result1 = "Result1";

        (function doThing2() {
            console.log("Thing 2 done");
            const result2 = "Result2 plus " + result1;

            (function doThing3() {
                console.log("Thing 3 done");
                const result3 = "Result3 plus " + result2;

                console.log("Final Result:", result3);
            })();
        })();
    })();
})();
