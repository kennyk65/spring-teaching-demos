(async function() {
    const delay = (ms) => new Promise(resolve => setTimeout(resolve, ms));

    async function doThing1() {
        const result1 = "Hello";
        await delay(1000);
        return result1;
    }

    async function doThing2(result1) {
        const result2 = result1 + " World";
        await delay(1000);
        return result2;
    }

    async function doThing3(result2) {
        const result3 = result2 + "!!";
        await delay(1000);
        return result3;
    }

    try {
        const result1 = await doThing1();
        const result2 = await doThing2(result1);
        const result3 = await doThing3(result2);

        console.log("Result from JavaScript:", result3);
    } catch (error) {
        console.error("An error occurred:", error);
    }
})();
