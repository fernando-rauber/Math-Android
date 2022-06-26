package uk.fernando.math.game

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import org.koin.test.inject
import uk.fernando.math.KoinTestCase
import uk.fernando.math.MainCoroutineRule
import uk.fernando.math.di.allMockedModules
import uk.fernando.math.util.QuestionGenerator
import uk.fernando.math.viewmodel.solo.GameViewModel
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SoloGameUnitTest : KoinTestCase() {
    private val gameViewModel: GameViewModel by inject()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    override val koinRule = KoinTestRule.create {
        modules(allMockedModules())
    }

    @Before
    fun setup() {
//        gameViewModel.createGame()
    }

    @Test
    fun `pause and unpause game`() {
        gameViewModel.pauseUnpauseGame()

        assertEquals(true, gameViewModel.isGamePaused.value)

        gameViewModel.pauseUnpauseGame()

        assertEquals(false, gameViewModel.isGamePaused.value)
    }

    @Test
    fun `check if jump to next question`() = runTest {
        gameViewModel.createGame()

        delay(2000)
        assertEquals(1, gameViewModel.nextQuestionCounter.value)

        gameViewModel.registerAnswer(1)

        assertEquals(2, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check if clean session and create a new session`() = runTest {
        delay(2000)
        gameViewModel.registerAnswer(1)
        assertEquals(2, gameViewModel.nextQuestionCounter.value)

        gameViewModel.createGame()

        assertEquals(1, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check if create data into database`() = runTest {
        gameViewModel.registerAnswer(1)
        gameViewModel.registerAnswer(2)
//        gameViewModel.registerAnswer(3)

        assertEquals(3, gameViewModel.nextQuestionCounter.value)

        delay(2000)
        assertEquals(true, gameViewModel.history.isFinished)
        assertEquals(true, gameViewModel.isGameFinished.value)
    }
}