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
import uk.fernando.math.viewmodel.multiplayer.MultiplayerGameViewModel
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MultiplayerGameUnitTest : KoinTestCase() {
    private val gameViewModel: MultiplayerGameViewModel by inject()

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
    fun `check if PLAYER 1 is awaiting after select answer`() = runTest {
        assertEquals(1, gameViewModel.nextQuestionCounter.value)

        gameViewModel.registerAnswerPlayer1(1)

        assertEquals(true, gameViewModel.player1Waiting.value)
        assertEquals(false, gameViewModel.player2Waiting.value)
        assertEquals(1, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check if PLAYER 2 is awaiting after select answer`() = runTest {
        assertEquals(1, gameViewModel.nextQuestionCounter.value)

        gameViewModel.registerAnswerPlayer2(1)

        assertEquals(true, gameViewModel.player2Waiting.value)
        assertEquals(false, gameViewModel.player1Waiting.value)
        assertEquals(1, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check after both player answer to jump to next question`() = runTest {
        assertEquals(1, gameViewModel.nextQuestionCounter.value)

        gameViewModel.registerAnswerPlayer1(1)
        gameViewModel.registerAnswerPlayer2(1)

        assertEquals(2, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check if clean session and create a new session`() = runTest {
        gameViewModel.registerAnswerPlayer1(1)
        gameViewModel.registerAnswerPlayer2(1)

        assertEquals(2, gameViewModel.nextQuestionCounter.value)

        gameViewModel.createGame()

        assertEquals(1, gameViewModel.nextQuestionCounter.value)
    }

    @Test
    fun `check if create data into database`() = runTest {
        gameViewModel.registerAnswerPlayer1(1)
        gameViewModel.registerAnswerPlayer2(1)

        gameViewModel.registerAnswerPlayer1(1)
        gameViewModel.registerAnswerPlayer2(1)

        gameViewModel.registerAnswerPlayer1(1)
        gameViewModel.registerAnswerPlayer2(1)

        assertEquals(3, gameViewModel.nextQuestionCounter.value)

        delay(2000)
        assertEquals(true, gameViewModel.isGameFinished.value)
    }
}