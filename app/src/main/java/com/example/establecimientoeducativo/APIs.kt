package com.example.establecimientoeducativo

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class APIs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_apis)

        initUI()
    }

    private fun initUI() {val etPokemon = findViewById<EditText>(R.id.etPokemon)
        val btApi = findViewById<Button>(R.id.btAPIs)
        btApi.setOnClickListener {
            val pokemonName = etPokemon.text.toString()
            pokeApi(pokemonName)
        }
    }

    private fun pokeApi(name: String) {
        val tvInfoPoke = findViewById<TextView>(R.id.tvInfoPoke)
        val etPokemon = findViewById<EditText>(R.id.etPokemon)
        val ivPokemon = findViewById<ImageView>(R.id.ivPokemon)
        val scrollView = findViewById<ScrollView>(R.id.svScroll)

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val pokemonResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.pokeApiService.getPokemon(name)
                }
                val pokemonSpeciesResponse = withContext(Dispatchers.IO) {
                    RetrofitInstance.pokeApiService.getPokemonSpecies(name)
                }

                if (pokemonResponse.isSuccessful && pokemonSpeciesResponse.isSuccessful) {
                    val pokemon= pokemonResponse.body()!!
                    val pokemonSpecies = pokemonSpeciesResponse.body()!!

                    val spanishFlavorTexts = pokemonSpecies.flavorTextEntries.filter {
                        it.language.name == "es"
                    }.map { it.flavorText }

                    val info = StringBuilder()
                    info.append("Nombre: ${pokemon.name}\n")
                    info.append("Pokedex: #${pokemon.id}\n")
                    info.append("Experiencia base: ${pokemon.baseExperience}\n")
                    info.append("Altura: ${pokemon.height * 10} cm\n")
                    info.append("Peso: ${pokemon.weight / 10} kg\n")

                    // Stats
                    info.append("\nStats:\n")
                    for (stat in pokemon.stats) {
                        info.append(" - ${stat.stat.name}: ${stat.baseStat}\n")
                    }

                    // Types
                    info.append("\nTipos:\n")
                    for (type in pokemon.types) {
                        info.append(" - ${type.type.name}\n")
                    }
                    // Species
                    info.append("\nEspecie:\n")
                    info.append(" - ${pokemon.species.name}\n")

                    if (spanishFlavorTexts.isNotEmpty()) {
                        info.append("\nDescripcion General:\n")
                        for (flavorText in spanishFlavorTexts) {
                            info.append(" - $flavorText\n")
                        }
                    } else {
                        info.append("\nNo se encontraron textos  en español.\n")
                    }

                    Glide.with(this@APIs)
                        .load(pokemon.sprites.frontDefault)
                        .into(ivPokemon)

                    val firstAbility = pokemon.abilities.firstOrNull()
                    if (firstAbility != null) {
                        val firstAbilityName = firstAbility.ability.name

                        val abilityDetailsResponse = withContext(Dispatchers.IO) {
                            RetrofitInstance.pokeApiService.getAbility(firstAbilityName)
                        }

                        if (abilityDetailsResponse.isSuccessful) {
                            val abilityDetails = abilityDetailsResponse.body()!!
                            val spanishEffectEntry = abilityDetails.flavorTextEntries.firstOrNull { entry ->
                                entry.language.name == "es"
                            }

                            info.append("\nHabilidad:\n")
                            info.append(" - ${firstAbilityName}\n")
                            if (spanishEffectEntry != null) {
                                info.append(" - Descripción: ${spanishEffectEntry.flavorText}\n")
                            } else {
                                info.append(" - Descripción en español no encontrada.\n")
                            }
                        } else {
                            info.append("\nError al obtener detalles de la habilidad.\n")
                        }
                    } else {
                        info.append("\nEste Pokémon no tiene habilidades.\n")
                    }

                    scrollView.setBackgroundColor(Color.parseColor("#80000000"))
                    tvInfoPoke.setTextColor(Color.WHITE)

                    tvInfoPoke.text = info.toString()

                } else {
                    tvInfoPoke.text = "Error al obtener datos del Pokémon. " +
                            "Código de error: ${pokemonResponse.code()} / ${pokemonSpeciesResponse.code()}"
                }
            } catch (e: Exception) {
                if (e is HttpException && e.code() == 404) {
                    tvInfoPoke.text = "Pokémon no encontrado"
                } else {
                    tvInfoPoke.text = "Error al obtener datos del Pokémon: ${e.message}"
                }
            }
        }
    }
}