package uk.fernando.math.history

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
class HistoryUnitTest : KoinTestCase() {
    private val gameViewModel: GameViewModel by inject()

    init {
        QuestionGenerator.generateQuestions(listOf(1, 2, 3), 3, false, 1)
    }

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    override val koinRule = KoinTestRule.create {
        modules(allMockedModules())
    }

    @Before
    fun setup() {
        gameViewModel.createGame()
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
        assertEquals(1, gameViewModel.nextQuestion.value)

        gameViewModel.registerAnswer(1)

        assertEquals(2, gameViewModel.nextQuestion.value)
    }

    @Test
    fun `check if clean session and create a new session`() = runTest {
        gameViewModel.registerAnswer(1)
        assertEquals(2, gameViewModel.nextQuestion.value)

        gameViewModel.createGame()

        assertEquals(1, gameViewModel.nextQuestion.value)
    }

    @Test
    fun `check if create data into database`() = runTest {
        gameViewModel.registerAnswer(1)
        gameViewModel.registerAnswer(2)
        gameViewModel.registerAnswer(3)

        assertEquals(3, gameViewModel.nextQuestion.value)

        delay(2000)
        assertEquals(5, gameViewModel.historyId.value)
    }
}