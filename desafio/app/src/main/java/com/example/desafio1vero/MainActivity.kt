package com.example.desafio1vero

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etMonto: EditText
    private lateinit var etPersonas: EditText
    private lateinit var rgPropinas: RadioGroup
    private lateinit var rb10: RadioButton
    private lateinit var rb15: RadioButton
    private lateinit var rb20: RadioButton
    private lateinit var rbOtro: RadioButton
    private lateinit var etOtroPorcentaje: EditText
    private lateinit var switchIva: Switch
    private lateinit var btnCalcular: Button
    private lateinit var btnLimpiar: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enlazar elementos
        etMonto = findViewById(R.id.etMonto)
        etPersonas = findViewById(R.id.etPersonas)
        rgPropinas = findViewById(R.id.rgPropinas)
        rb10 = findViewById(R.id.rb10)
        rb15 = findViewById(R.id.rb15)
        rb20 = findViewById(R.id.rb20)
        rbOtro = findViewById(R.id.rbOtro)
        etOtroPorcentaje = findViewById(R.id.etOtroPorcentaje)
        switchIva = findViewById(R.id.switchIva)
        btnCalcular = findViewById(R.id.btnCalcular)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        tvResultado = findViewById(R.id.tvResultado)

        // Mostrar/ocultar campo "otro porcentaje"
        rbOtro.setOnCheckedChangeListener { _, isChecked ->
            etOtroPorcentaje.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        btnCalcular.setOnClickListener {
            calcularPropina()
        }

        btnLimpiar.setOnClickListener {
            limpiarCampos()
        }
    }

    private fun calcularPropina() {
        val monto = etMonto.text.toString().toDoubleOrNull()
        val personas = etPersonas.text.toString().toIntOrNull()

        if (monto == null || personas == null || personas <= 0) {
            Toast.makeText(this, "Ingresa valores válidos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener porcentaje
        val porcentaje = when (rgPropinas.checkedRadioButtonId) {
            R.id.rb10 -> 10.0
            R.id.rb15 -> 15.0
            R.id.rb20 -> 20.0
            R.id.rbOtro -> etOtroPorcentaje.text.toString().toDoubleOrNull() ?: 0.0
            else -> 0.0
        }

        // Calcular propina
        var total = monto + (monto * (porcentaje / 100))

        // Agregar IVA si aplica
        if (switchIva.isChecked) {
            total += monto * 0.16
        }

        val propinaTotal = total - monto
        val totalPorPersona = total / personas

        // Mostrar resultados
        tvResultado.text = """
            Propina total: $${"%.2f".format(propinaTotal)}
            Total a pagar: $${"%.2f".format(total)}
            Total por persona: $${"%.2f".format(totalPorPersona)}
        """.trimIndent()
    }

    private fun limpiarCampos() {
        etMonto.text.clear()
        etPersonas.text.clear()
        rgPropinas.clearCheck()
        etOtroPorcentaje.text.clear()
        etOtroPorcentaje.visibility = View.GONE
        switchIva.isChecked = false
        tvResultado.text = ""
    }
}
