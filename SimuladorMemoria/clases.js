class Memoria {
    constructor(tamano) {
        this.mem = tamano
        this.segmentos = [{"proceso": null, "tamano": tamano, "posicion": "100000"}]
    }

    getSegmentos() {
        return this.segmentos;
    }

    getProceso(id) {
        var proceso = [];

        for (let index = 0; index < this.segmentos.length; index++) {
            const segmento = this.segmentos[index];
            if (segmento.proceso != null) {
                if (segmento.proceso.id == id) {
                    proceso.push(segmento);
                }
            }
        }
        return proceso;
    }

    getMemoriaDisponible() {
        var count = 0;
        this.segmentos.forEach(segmento => {
            if (segmento.proceso == null) {
                count += segmento.tamano;
            }
        });
        return count;
    }

    getSegmentosLibres() {
        var segmentosLibres = [];

        this.segmentos.forEach(segmento => {
            if (segmento.proceso == null) {
                segmentosLibres.push(segmento);
            }
        });

        return segmentosLibres;
    }

    setMetodoFija(segmentos) {
        const tamSeg = this.segmentos[0].tamano / segmentos;
        var posicion = 1048576;
        this.segmentos[0].tamano = tamSeg;
        for (let index = 0; index < segmentos - 1; index++) {
            posicion = posicion + tamSeg;
            this.segmentos.push({"proceso": null, "tamano": tamSeg, "posicion": componentToHex(Math.ceil(posicion))});
        }
    }

    setMetodoVariable(segmentos) {
        const mega = 1048576;
        var posicion = 1048576;
        this.segmentos[0].tamano = mega * segmentos[0];
        for (let index = 1; index < segmentos.length; index++) {
            posicion = posicion + mega * segmentos[index - 1];
            this.segmentos.push({
                "proceso": null,
                "tamano": mega * segmentos[index],
                "posicion": componentToHex(Math.ceil(posicion))
            });
        }
    }

    eliminarProceso(id, nombre, gestionMemoria) {
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];

            if (element.proceso != null) {
                if (element.proceso.id == id && element.proceso.nombre == nombre) {
                    this.segmentos[index].proceso = null;
                }
            }
        }

        /// Evalua los metodos de dinamica
        switch (gestionMemoria) {
            case 2: /// Sin compactación
                this.dividirMemoria();
                break;
            case 1: /// Con compactación
                this.dividirMemoria();
                this.compactarMemoria();
                break;
        }

        this.actualizarMemoriaDisponible()
        this.actualizarMemoriaOcupada();
    }

    eliminarProcesoPag(id) {
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];

            if (element.proceso != null) {
                if (element.proceso.id == id) {
                    this.segmentos[index].proceso = null;
                }
            }
        }
        this.dividirMemoria();
    }

    actualizarMemoriaDisponible() {
        const memoriaDisponibleElemento = document.getElementById("memoria-disponible-valor");
        const memoriaDisponible = this.getMemoriaDisponible();
        memoriaDisponibleElemento.textContent = memoriaDisponible;
    }

    actualizarMemoriaOcupada() {
        const memoriaOcupadaElemento = document.getElementById("memoria-ocupada-valor");
        const memoriaOcu = this.mem - this.getMemoriaDisponible();
        memoriaOcupadaElemento.textContent = memoriaOcu;
    }

    cabeSegmento(proceso) {
        var cabe = false;
        var segmentosLibres = [];
        for (let index = 0; index < this.segmentos.length; index++) {
            const segmento = this.segmentos[index];
            if (segmento.proceso == null) {
                segmentosLibres.push(segmento);
            }
        }

        for (let index = 0; index < segmentosLibres.length; index++) {
            const segmento = segmentosLibres[index];
            if (segmento.tamano >= proceso.tamano) {
                cabe = true;
                break;
            }
            cabe = false;
        }

        return cabe;
    }

    insertarProceso(proceso, metodo, seleccionAjuste) {

        /// Segmentación
        if (metodo == 5) {
            if (this.getMemoriaDisponible() == 0) {
                return 0;
            }
            if (this.getMemoriaDisponible() < proceso.tamano) {
                return 1;
            }

            return this.segmentarProceso(proceso, seleccionAjuste);
        }

        /// Metodo estatico fijo
        if (metodo == 4) {
            return this.estaticaFija(proceso);
        }

        /// Evalua segun el ajuste deseado
        var resultado = null;
        if (seleccionAjuste == 'primer') {
            resultado = this.primerAjuste(proceso);
        } else if (seleccionAjuste == 'peor') {
            resultado = this.peorAjuste(proceso);
        } else if (seleccionAjuste == 'mejor') {
            resultado = this.mejorAjuste(proceso);
        }

        /// Evalua los metodos de dinamica
        if (metodo == 1 || metodo == 2) {
            /// Sí hubi algún error en el llenadod el proceso
            if (resultado == 1 || resultado == 0) {
                return resultado;
            }
            const resultadoDividir = this.dividirMemoria();
            this.actualizarMemoriaDisponible(); // Actualizar la memoria disponible después de dividir la memoria
            this.actualizarMemoriaOcupada();
            return resultadoDividir;
        }

        this.actualizarMemoriaDisponible();
        this.actualizarMemoriaOcupada();

        return resultado;
    }

    primerAjuste(proceso) {
        // return 1 si el proceso no cabe en el segmento
        // return 0 si la memoria esta llena
        var memoriaLlena = true;
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];

            if (element.proceso === null) {
                memoriaLlena = false;
                if (element.tamano >= proceso.tamano) {
                    this.segmentos[index].proceso = proceso;
                    return this.segmentos;
                }
            }
        }

        if (memoriaLlena)
            return 0;

        return 1;
    }

    peorAjuste(proceso) {
        // return 1 si el proceso no cabe en el segmento
        // return 0 si la memoria esta llena
        var memoriaLlena = true;
        var segmento = 0;
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];
            if (element.proceso === null) {
                if (element.tamano >= parseInt(proceso.tamano)) {
                    segmento = index;
                }
                memoriaLlena = false;
            }
        }

        if (segmento == 0 && this.segmentos[segmento].proceso != null) {
            return 1;
        }

        if (memoriaLlena) {
            return 0;
        }

        if (this.segmentos[segmento].tamano >= proceso.tamano) {
            this.segmentos[segmento].proceso = proceso;
            return this.segmentos;
        }

        return 1;
    }

    mejorAjuste(proceso) {
        // return 1 si el proceso no cabe en el segmento
        // return 0 si la memoria esta llena
        var mejor = 1048576 * 15;
        var cabe = false;
        var memoriaLlena = true;
        var segmento = 0;

        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];
            var dif = element.tamano - proceso.tamano;

            if (element.proceso === null) {
                if (dif < mejor && dif >= 0) {
                    mejor = dif;
                    segmento = index;
                    cabe = true;
                }
                memoriaLlena = false;
            }
        }

        if (memoriaLlena) {
            return 0;
        }

        if (cabe) {
            this.segmentos[segmento].proceso = proceso;
            return this.segmentos;
        }

        return 1;

    }

    estaticaFija(proceso) {
        // return 1 si el proceso no cabe en el segmento
        // return 0 si la memoria esta llena
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];

            if (element.proceso === null) {
                if (element.tamano < proceso.tamano) {
                    return 1;
                }
                this.segmentos[index].proceso = proceso;
                return this.segmentos;
            }
        }
        return 0;
    }


    dividirMemoria() {
        // no borrar!!!!!
        /// Dividir la memoria en el tamaño de los segmentos
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];
            if (element.proceso !== null) {
                if (element.proceso.tamano < element.tamano) {
                    var nuevoSeg = element.tamano - element.proceso.tamano;
                    var posicion = parseInt(element.posicion, 16) + parseInt(element.proceso.tamano);

                    element.tamano = parseInt(element.proceso.tamano);
                    this.segmentos.splice(index + 1, 0, {
                        "proceso": null,
                        "tamano": nuevoSeg,
                        "posicion": componentToHex(Math.ceil(posicion))
                    });
                }
            }
        }

        /// Unir los segmentos con procesos vacios
        for (let index = 0; index < this.segmentos.length; index++) {
            const element = this.segmentos[index];
            if (element.proceso === null) {
                if (index + 1 < this.segmentos.length) {
                    if (this.segmentos[index + 1].proceso === null) {
                        var nuevoSeg = element.tamano + this.segmentos[index + 1].tamano;
                        var posicion = parseInt(element.posicion, 16);

                        element.tamano = nuevoSeg;
                        this.segmentos.splice(index + 1, 1);
                        index -= 1;
                    }
                }
            }
        }
        return this.segmentos;
    }


}

class Proceso {
    constructor(id, nombre, tamano) {
        this.id = id
        this.nombre = nombre
        this.tamano = tamano
    }

    getId() {
        return this.id;
    }

    getNombre() {
        return this.nombre;
    }

    getTamano() {
        return this.tamano;
    }

    getPosicion() {
        return this.posicion;
    }
}
