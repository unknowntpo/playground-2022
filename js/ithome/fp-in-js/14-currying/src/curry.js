
function add(x) {
    return (y) => {
        return x + y
    }
}

add_one = add(1)

describe("addOne", () => {
    test("apply add_one to 2", () => {
        input = [1, 2, 3, 4]
        expect(
            add_one(2)
        ).toEqual(3)
    })
})

const uncurriedGradeTest = (passGrade, failGrade, average, testScore) => testScore >= average ? passGrade : failGrade;

describe("uncurriedGradeTest", () => {
    test("FAIL", () => {
        expect(
            uncurriedGradeTest('PASS', 'FAIL', 0.2, 0.19)
        ).toEqual("FAIL")
    })
    test("PASS", () => {
        expect(
            uncurriedGradeTest('PASS', 'FAIL', 0.2, 0.39)
        ).toEqual("PASS")
    })
})

const getGradeTest = (passGrade, failGrade) => {
    return (average) => {
        return (testScore) => {
            return testScore >= average ? passGrade : failGrade;
        }
    }
}

const passFailTest = getGradeTest('PASS', 'FAIL')(0.2);

describe("curriedGradeTest", () => {
    test("FAIL", () => {
        expect(
            passFailTest(0.19)
        ).toEqual("FAIL")
    })
    test("PASS", () => {
        expect(
            passFailTest(0.39)
        ).toEqual("PASS")
    })
})