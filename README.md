# WordleSolver
## Run Wordle
The basic run command is ``./gradlew run --args="wordle <args>"`` where args are your guesses.
For example if you score this on the wordle:
<img src="https://github.com/brendancron/WordleSolver/blob/main/misc/blend.png" width="200" height="200" />
Your command would be: ``./gradlew run --args="wordle b0l2e1n0d0"`` The 0 means that the letter does not exist (different if multiple), 1 means the letter is in the wrong spot and 2 means the letter is correctly placed.
This command will provide the following output after you run it: 
``[<word, score>, ...]`` in order of the guesses with the most "entropy". For this example it suggests that you guess slake next.

## Run Duotrigordle
The command to run duotrigordle is ``./gradlew run --args="duotrigordle <args>"`` where the args are your preliminary guesses. It is highlly recommended that you add at least a starter guess so the calculation time for the second step is minimal although it may work without a first guess provided you are patient to wait for the calculation of maximum entropy. It may be more efficient to add a second guess as well. I have not calculated all of the expected results.