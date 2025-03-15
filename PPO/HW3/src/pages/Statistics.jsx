import React, {useEffect, useState} from 'react';
import Queries from "../CQRS/Queries.ts";

const Statistics = () => {
    const [trainsCount, setTrainsCount] = useState([])
    const [trainsTime, setTrainsTime] = useState([])
    const options = {weekday: 'long'}

    const rounded = (number) => +number.toFixed(2);

    useEffect(() => {
        Queries.getAllInfoTrain().then(e => {
            const mapWithCountTrain = new Map()
            const mapWithTimeTrain = new Map()
            let initArr = [0, 1, 2, 3, 4, 5, 6]
            initArr.forEach(t => {
                const newDate = new Date()
                newDate.setDate(t)
                let key = new Intl.DateTimeFormat('ru-RU', options).format(newDate)
                mapWithCountTrain.set(key, 0)
                mapWithTimeTrain.set(key, 0)
            })
            e.forEach(day => {
                let key = new Intl.DateTimeFormat('ru-RU', options).format(day.start)
                mapWithCountTrain.set(key, mapWithCountTrain.get(key) + 1)
                mapWithTimeTrain.set(key, mapWithTimeTrain.get(key) + (day.end - day.start))
            })
            let array = Array.from(mapWithCountTrain, ([name, value]) => ({name, value}))
            let array1 = Array.from(mapWithTimeTrain, ([name, value]) => ({name, value}))
            array1.map(e => {
                e.value = rounded(e.value / (Math.max(mapWithCountTrain.get(e.name), 1) * 1000 * 60))
            })
            setTrainsCount(array)
            setTrainsTime(array1)
        })
    }, [])

    return (
        <div>
            <h1>Количнство посещений по дням недели : </h1>
            {trainsCount.map(e => <div key={Math.random()} style={{width: "200px", display: "flex", justifyContent: "space-between"}}>
                <div>{e.name}</div>
                <div>{e.value}</div>
            </div>)
            }
            <h1>Среднее время в зале в минутах : </h1>
            {trainsTime.map(e => <div key={Math.random()} style={{width: "200px", display: "flex", justifyContent: "space-between"}}>
                <div>{e.name}</div>
                <div>{e.value}</div>
            </div>)
            }
        </div>
    );
};

export default Statistics;