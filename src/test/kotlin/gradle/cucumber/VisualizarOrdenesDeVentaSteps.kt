package gradle.cucumber

import ar.unq.unqtrading.dto.OrdenDeVentaDTO
import ar.unq.unqtrading.entities.Empresa
import ar.unq.unqtrading.entities.OrdenDeVenta
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.Assert
import org.junit.runner.RunWith
import org.springframework.web.client.getForObject
import java.time.LocalDate

@RunWith(Cucumber::class)
@CucumberOptions(features = ["src/test/resources"])
class VisualizarOrdenesDeVentaSteps : SpringIntegrationTest(){
    lateinit var response: OrdenDeVenta
    lateinit var listResponse: List<OrdenDeVenta>
    var orden1 = OrdenDeVentaDTO()
    var orden2 = OrdenDeVentaDTO()

    @Given("una empresa con nombre {string} y {int} ordenes de venta")
    fun una_empresa_con_nombre_y_ordenes_de_venta(nombre: String, cant: Int) {
        val url = "$DEFAULT_URL/save"
        val saveEmpresa = "$EMPRESA_URL/register"

        var empresaEntity = Empresa()
        empresaEntity.nombreEmpresa = nombre
        empresaEntity.email = "test@test.com"
        empresaEntity.cuit = 12345
        empresaEntity.password = "123124"
        empresaEntity = restTemplate.postForObject(saveEmpresa, empresaEntity, Empresa::class.java) as Empresa

        orden1.nombreEmpresa = nombre
        orden1.cantidadDeAcciones = 1000
        orden1.precio = 500
        orden1.fechaDeCreacion = LocalDate.now()
        orden1.fechaDeVencimiento = LocalDate.of(2020, 7, 25)
        orden1.creadorId = empresaEntity.id
        orden2.nombreEmpresa = nombre
        orden2.cantidadDeAcciones = 1000
        orden2.precio = 500
        orden2.fechaDeCreacion = LocalDate.now()
        orden2.fechaDeVencimiento = LocalDate.of(2020, 7, 25)
        orden2.creadorId = empresaEntity.id
        restTemplate.postForObject(url, orden1, OrdenDeVenta::class.java)!!
        restTemplate.postForObject(url, orden2, OrdenDeVenta::class.java)!!
    }

    @When("pregunto cuales son sus ordenes de venta")
    fun pregunto_cuales_son_sus_ordenes_de_venta() {
        val url = "$DEFAULT_URL/all?nombreEmpresa=UNQ"
        listResponse = restTemplate.getForObject(url)!!
    }

    @Then("debo visualizar {int} ordenes de venta")
    fun debo_visualizar_ordenes_de_venta(cantidad: Int?) {

        Assert.assertEquals(2, listResponse.size)
    }

    @Given("una empresa con nombre {string}")
    fun crearOrdenDeVenta(nombre: String) {

        var empresa = Empresa()
        empresa.nombreEmpresa = "nombreDeEmpresa"
        empresa.email = "mailDeEmpresa@test.com"
        empresa.cuit = 12345645600
        empresa.password = "123124"
        empresa = restTemplate.postForObject("$EMPRESA_URL/register", empresa, Empresa::class.java) as Empresa

        orden1 = OrdenDeVentaDTO()
        orden1.nombreEmpresa = nombre
        orden1.cantidadDeAcciones = 1000
        orden1.precio = 500
        orden1.fechaDeCreacion = LocalDate.now()
        orden1.fechaDeVencimiento = LocalDate.of(2020, 7, 25)
        orden1.creadorId = empresa.id
    }

    @When("creo una orden de venta")
    fun postOrdenDeVenta() {
        val url = "$DEFAULT_URL/save"
        response = restTemplate.postForObject(url, orden1, OrdenDeVenta::class.java)!!
    }

    @Then("la orden de venta tiene un id")
    fun assertId() {
        Assert.assertEquals(3, response.id)
    }
}