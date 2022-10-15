// import React, { useState } from 'react';
// import ReactDOM from "react-dom/client";


// const App = () => {
//     const [count, setCount] = useState(0)

//     return (
//         <div>
//             <p>You clicked {count} times</p>
//             <button onClick={() => setCount(count + 1)}>
//                 Click Me
//             </button>
//         </div>
//     )
// }
// const root = ReactDOM.createRoot(document.getElementById('root'));

// root.render(<App />);

import { useState } from "react";
import ReactDOM from "react-dom/client";

function Car() {
    const [car, setCar] = useState({
        brand: "Ford",
        model: "Mustang",
        year: "1964",
        color: "red"
    });

    const updateColor = () => {
        setCar(previousState => {
            return { ...previousState, color: "blue" }
        });
    }

    return (
        <>
            <h1>My {car.brand}</h1>
            <p>
                It is a {car.color} {car.model} from {car.year}.
            </p>
            <button
                type="button"
                onClick={updateColor}
            >Blue</button>
        </>
    )
}

function Clicker() {
    // Declare a new state variable, which we'll call "count"
    const [count, setCount] = useState(0);

    return (
        <div>
            <p>You clicked {count} times</p>
            <button onClick={() => setCount(count + 1)}>
                Click me
            </button>
        </div>
    );
}

function Switch() {
    const [sw, setSwitch] = useState(0);

    return (
        <div>
            <button onClick={() => {
                setSwitch(!sw);
                console.log("current sw", sw);
            }}>
                OnOff
            </button>
        </div >
    );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(<Clicker />);
root.render(<Switch />);