let fechaActual = new Date();

let renderCalendar = () => {

    let fecha = new Date();

    //Para usar innerHTML
    let diasDelMes = document.querySelector('.dias');

    // cantidad de dias en el mes
    let totalDiasMes = new Date(fechaActual.getFullYear(), fechaActual.getMonth() + 1, 0).getDate();

    // ante ultimo dia del mes
    let previoUltimoDia = new Date(fechaActual.getFullYear(), fechaActual.getMonth(), 0).getDate();

    // Dia del primer dia del mes
    let primerDiaMes = new Date(fechaActual.getFullYear(), fechaActual.getMonth(), 1).getDay();
   
    //Dia del ultimo dia del mes

    let ultimoDiaMes = new Date(fechaActual.getFullYear(), fechaActual.getMonth() + 1 , 0).getDay();

    let indexTotalDiasMes = new Date(fechaActual.getFullYear(), fechaActual.getMonth() + 1, 0).getDate();

    //dias que quedan en el mes
    let diasRestantes = -(7 - indexTotalDiasMes - 1);

    console.log(ultimoDiaMes);
    


    const MESES = [
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    ];

    const DIAS = [
        "Domingo",
        "Lunes",
        "Martes",
        "Miercoles",
        "Jueves",
        "Viernes",
        "Sabado"
    ];

    document.querySelector(".mes").innerHTML = MESES[fechaActual.getMonth()];
    document.querySelector(".today").innerHTML = (DIAS[fecha.getDay()] + '  ' + fecha.getDate() + '  de ' + MESES[fecha.getMonth()] + ',  ' + fecha.getFullYear());

    let dias = "";

    
    //funcion para dias previos del mes anterior !!REVISAR!!!
    for (let x = primerDiaMes; x > 0; x--) {
        dias += `<div class="dias-previos">${previoUltimoDia - x + 1}</div>`
        
    }


    //si el dia coincide con el actual se agrega la class hoy, si no se asignan los numeros de los dias
    for (let i = 1; i <= totalDiasMes; i++) {
        
        if (i == new Date().getDate() && fechaActual.getMonth() == new Date().getMonth() && fechaActual.getFullYear() == new Date().getFullYear()) {
            dias += `<div class="hoy">${i}</div>`
        } else {
            dias += `<div>${i}</div>`
        }
        
    }

    
    let j =1;

    while (ultimoDiaMes != 6) {
        
        dias += `<div class="dias-siguientes">${j}</div>`;
        j++;
        ultimoDiaMes++;
    }
    


    diasDelMes.innerHTML = dias;

}



document.querySelector('.anterior').addEventListener('click', () => {
    fechaActual.setMonth(fechaActual.getMonth() - 1);
    renderCalendar();
});

document.querySelector('.siguiente').addEventListener('click', () => {
    fechaActual.setMonth(fechaActual.getMonth() + 1);
    renderCalendar();
});

renderCalendar();




