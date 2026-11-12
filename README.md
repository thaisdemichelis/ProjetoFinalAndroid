# PetCusto

App Android (Kotlin + Jetpack Compose) para **calcular e salvar orçamentos de serviços para pets** (banho, consulta e vacina) de acordo com a raça e o peso do animal.

## Telas
1. **Splash** – decide automaticamente entre Login e Raças, conforme a sessão salva.
2. **Login** e 3. **Cadastro** – validação de campos (senha com no mínimo 6 caracteres).
4. **Raças** – lista de raças com foto vinda da Dog CEO API.
5. **Detalhe** – foto da raça e botão "Fazer orçamento para esta raça".
6. **Calcular** – raça, tratamento, peso e valor estimado em tempo real; salva o orçamento.
7. **Salvos** – lista de orçamentos, exclusão e botão **Sair**.

## Regra de preço
`valor = preço base do tratamento × multiplicador de peso`

| Tratamento | Base | | Peso | Multiplicador |
|---|---|---|---|---|
| Banho | R$ 40 | | menos de 10 kg | 1,0 |
| Consulta | R$ 120 | | 10 a 25 kg | 1,5 |
| Vacina | R$ 90 | | acima de 25 kg | 2,0 |

## Tecnologias
Navigation Compose (rotas type-safe), Bottom Navigation de 3 abas, ViewModel único, Retrofit + kotlinx.serialization, Coil, DataStore.

## Como rodar no Android Studio
1. `File > Open` e selecione a pasta do projeto.
2. Aguarde o **Gradle Sync** (se o Android Studio pedir, aceite criar o Gradle Wrapper).
3. Escolha um emulador (API 24+, Android 7.0 Nougat) ou celular e clique em **Run**.
4. Conexão com a internet é necessária para carregar as fotos das raças.

## Estrutura
```
app/src/main/java/com/petcusto/app
├── MainActivity.kt
├── data/{model,remote,local,repository}
├── domain/PriceCalculator.kt
├── util/Format.kt
├── viewmodel/AppViewModel.kt
└── ui/{theme,components,navigation,screens}
```
