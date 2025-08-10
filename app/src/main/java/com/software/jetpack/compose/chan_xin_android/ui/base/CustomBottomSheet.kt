package com.software.jetpack.compose.chan_xin_android.ui.base

import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.Expanded
import androidx.compose.material.ModalBottomSheetValue.HalfExpanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.collapse
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.expand
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt
@ExperimentalMaterialApi
@Composable
fun rememberModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    confirmValueChange: (ModalBottomSheetValue) -> Boolean = { true },
    skipHalfExpanded: Boolean = false,
): ModalBottomSheetState {

    val density = LocalDensity.current
    // Key the rememberSaveable against the initial value. If it changed we don't want to attempt
    // to restore as the restored value could have been saved with a now invalid set of anchors.
    // b/152014032
    return key(initialValue) {
        rememberSaveable(
            initialValue, animationSpec, skipHalfExpanded, confirmValueChange, density,
            saver = ModalBottomSheetState.Saver(
                density = density,
                animationSpec = animationSpec,
                skipHalfExpanded = skipHalfExpanded,
                confirmValueChange = confirmValueChange
            )
        ) {
            ModalBottomSheetState(
                density = density,
                initialValue = initialValue,
                animationSpec = animationSpec,
                isSkipHalfExpanded = skipHalfExpanded,
                confirmValueChange = confirmValueChange
            )
        }
    }
}
@Composable
@ExperimentalMaterialApi
fun rememberModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    skipHalfExpanded: Boolean,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean,
): ModalBottomSheetState = rememberModalBottomSheetState(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmValueChange = confirmStateChange,
    skipHalfExpanded = skipHalfExpanded
)
internal typealias InternalAtomicReference<V> =
        java.util.concurrent.atomic.AtomicReference<V>
@ExperimentalMaterialApi
internal interface DraggableAnchors<T> {

    /**
     * Get the anchor position for an associated [value]
     *
     * @return The position of the anchor, or [Float.NaN] if the anchor does not exist
     */
    fun positionOf(value: T): Float

    /**
     * Whether there is an anchor position associated with the [value]
     *
     * @param value The value to look up
     * @return true if there is an anchor for this value, false if there is no anchor for this value
     */
    fun hasAnchorFor(value: T): Boolean

    /**
     * Find the closest anchor to the [position].
     *
     * @param position The position to start searching from
     *
     * @return The closest anchor or null if the anchors are empty
     */
    fun closestAnchor(position: Float): T?

    /**
     * Find the closest anchor to the [position], in the specified direction.
     *
     * @param position The position to start searching from
     * @param searchUpwards Whether to search upwards from the current position or downwards
     *
     * @return The closest anchor or null if the anchors are empty
     */
    fun closestAnchor(position: Float, searchUpwards: Boolean): T?

    /**
     * The smallest anchor, or [Float.NEGATIVE_INFINITY] if the anchors are empty.
     */
    fun minAnchor(): Float

    /**
     * The biggest anchor, or [Float.POSITIVE_INFINITY] if the anchors are empty.
     */
    fun maxAnchor(): Float

    /**
     * The amount of anchors
     */
    val size: Int
}
@Stable
internal class InternalMutatorMutex {
    private class Mutator(val priority: MutatePriority, val job: Job) {
        fun canInterrupt(other: Mutator) = priority >= other.priority

        fun cancel() = job.cancel()
    }

    private val currentMutator = InternalAtomicReference<Mutator?>(null)
    private val mutex = Mutex()

    private fun tryMutateOrCancel(mutator: Mutator) {
        while (true) {
            val oldMutator = currentMutator.get()
            if (oldMutator == null || mutator.canInterrupt(oldMutator)) {
                if (currentMutator.compareAndSet(oldMutator, mutator)) {
                    oldMutator?.cancel()
                    break
                }
            } else throw CancellationException("Current mutation had a higher priority")
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutate] is called while another call to [mutate] or [mutateWith] is in progress, their
     * [priority] values are compared. If the new caller has a [priority] equal to or higher than
     * the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * @param priority the priority of this mutation; [MutatePriority.Default] by default.
     * Higher priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     */
    suspend fun <R> mutate(
        priority: MutatePriority = MutatePriority.Default,
        block: suspend () -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Enforce that only a single caller may be active at a time.
     *
     * If [mutateWith] is called while another call to [mutate] or [mutateWith] is in progress,
     * their [priority] values are compared. If the new caller has a [priority] equal to or
     * higher than the call in progress, the call in progress will be cancelled, throwing
     * [CancellationException] and the new caller's [block] will be invoked. If the call in
     * progress had a higher [priority] than the new caller, the new caller will throw
     * [CancellationException] without invoking [block].
     *
     * This variant of [mutate] calls its [block] with a [receiver], removing the need to create
     * an additional capturing lambda to invoke it with a receiver object. This can be used to
     * expose a mutable scope to the provided [block] while leaving the rest of the state object
     * read-only. For example:
     *
     * @param receiver the receiver `this` that [block] will be called with
     * @param priority the priority of this mutation; [MutatePriority.Default] by default.
     * Higher priority mutations will interrupt lower priority mutations.
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     */
    suspend fun <T, R> mutateWith(
        receiver: T,
        priority: MutatePriority = MutatePriority.Default,
        block: suspend T.() -> R
    ) = coroutineScope {
        val mutator = Mutator(priority, coroutineContext[Job]!!)

        tryMutateOrCancel(mutator)

        mutex.withLock {
            try {
                receiver.block()
            } finally {
                currentMutator.compareAndSet(mutator, null)
            }
        }
    }

    /**
     * Attempt to mutate synchronously if there is no other active caller.
     * If there is no other active caller, the [block] will be executed in a lock. If there is
     * another active caller, this method will return false, indicating that the active caller
     * needs to be cancelled through a [mutate] or [mutateWith] call with an equal or higher
     * mutation priority.
     *
     * Calls to [mutate] and [mutateWith] will suspend until execution of the [block] has finished.
     *
     * @param block mutation code to run mutually exclusive with any other call to [mutate],
     * [mutateWith] or [tryMutate].
     * @return true if the [block] was executed, false if there was another active caller and the
     * [block] was not executed.
     */
    fun tryMutate(block: () -> Unit): Boolean {
        val didLock = mutex.tryLock()
        if (didLock) {
            try {
                block()
            } finally {
                mutex.unlock()
            }
        }
        return didLock
    }
}
@OptIn(ExperimentalMaterialApi::class)
private class MapDraggableAnchors<T>(private val anchors: Map<T, Float>) :
    DraggableAnchors<T> {

    override fun positionOf(value: T): Float = anchors[value] ?: Float.NaN
    override fun hasAnchorFor(value: T) = anchors.containsKey(value)

    override fun closestAnchor(position: Float): T? = anchors.minByOrNull {
        abs(position - it.value)
    }?.key

    override fun closestAnchor(
        position: Float,
        searchUpwards: Boolean
    ): T? {
        return anchors.minByOrNull { (_, anchor) ->
            val delta = if (searchUpwards) anchor - position else position - anchor
            if (delta < 0) Float.POSITIVE_INFINITY else delta
        }?.key
    }

    override fun minAnchor() = anchors.values.minOrNull() ?: Float.NaN

    override fun maxAnchor() = anchors.values.maxOrNull() ?: Float.NaN

    override val size: Int
        get() = anchors.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MapDraggableAnchors<*>) return false

        return anchors == other.anchors
    }

    override fun hashCode() = 31 * anchors.hashCode()

    override fun toString() = "com.software.jetpack.compose.chan_xin_android.ui.base.MapDraggableAnchors($anchors)"
}
private fun <T> emptyDraggableAnchors() = MapDraggableAnchors<T>(emptyMap())
private class AnchoredDragFinishedSignal : CancellationException() {
    override fun fillInStackTrace(): Throwable {
        stackTrace = emptyArray()
        return this
    }
}
private suspend fun <I> restartable(inputs: () -> I, block: suspend (I) -> Unit) {
    try {
        coroutineScope {
            var previousDrag: Job? = null
            snapshotFlow(inputs)
                .collect { latestInputs ->
                    previousDrag?.apply {
                        cancel(AnchoredDragFinishedSignal())
                        join()
                    }
                    previousDrag = launch(start = CoroutineStart.UNDISPATCHED) {
                        block(latestInputs)
                        this@coroutineScope.cancel(AnchoredDragFinishedSignal())
                    }
                }
        }
    } catch (anchoredDragFinished: AnchoredDragFinishedSignal) {
        // Ignored
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
internal suspend fun <T> AnchoredDraggableState<T>.animateTo(
    targetValue: T,
    velocity: Float = this.lastVelocity,
) {
    anchoredDrag(targetValue = targetValue) { anchors, latestTarget ->
        val targetOffset = anchors.positionOf(latestTarget)
        if (!targetOffset.isNaN()) {
            var prev = if (offset.isNaN()) 0f else offset
            animate(prev, targetOffset, velocity, animationSpec) { value, velocity ->
                // Our onDrag coerces the value within the bounds, but an animation may
                // overshoot, for example a spring animation or an overshooting interpolator
                // We respect the user's intention and allow the overshoot, but still use
                // DraggableState's drag for its mutex.
                dragTo(value, velocity)
                prev = value
            }
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
internal suspend fun <T> AnchoredDraggableState<T>.snapTo(targetValue: T) {
    anchoredDrag(targetValue = targetValue) { anchors, latestTarget ->
        val targetOffset = anchors.positionOf(latestTarget)
        if (!targetOffset.isNaN()) dragTo(targetOffset)
    }
}
@Stable
@ExperimentalMaterialApi
internal class AnchoredDraggableState<T>(
    initialValue: T,
    internal val positionalThreshold: (totalDistance: Float) -> Float,
    internal val velocityThreshold: () -> Float,
    val animationSpec: AnimationSpec<Float>,
    internal val confirmValueChange: (newValue: T) -> Boolean = { true }
) {

    /**
     * Construct an [AnchoredDraggableState] instance with anchors.
     *
     * @param initialValue The initial value of the state.
     * @param anchors The anchors of the state. Use [updateAnchors] to update the anchors later.
     * @param animationSpec The default animation that will be used to animate to a new state.
     * @param confirmValueChange Optional callback invoked to confirm or veto a pending state
     * change.
     * @param positionalThreshold The positional threshold, in px, to be used when calculating the
     * target state while a drag is in progress and when settling after the drag ends. This is the
     * distance from the start of a transition. It will be, depending on the direction of the
     * interaction, added or subtracted from/to the origin offset. It should always be a positive
     * value.
     * @param velocityThreshold The velocity threshold (in px per second) that the end velocity has
     * to exceed in order to animate to the next state, even if the [positionalThreshold] has not
     * been reached.
     */
    @ExperimentalMaterialApi
    constructor(
        initialValue: T,
        anchors: DraggableAnchors<T>,
        positionalThreshold: (totalDistance: Float) -> Float,
        velocityThreshold: () -> Float,
        animationSpec: AnimationSpec<Float>,
        confirmValueChange: (newValue: T) -> Boolean = { true }
    ) : this(
        initialValue,
        positionalThreshold,
        velocityThreshold,
        animationSpec,
        confirmValueChange
    ) {
        this.anchors = anchors
        trySnapTo(initialValue)
    }

    private val dragMutex = InternalMutatorMutex()

    @OptIn(ExperimentalFoundationApi::class)
    internal val draggableState = object : DraggableState {

        private val dragScope = object : DragScope {
            override fun dragBy(pixels: Float) {
                with(anchoredDragScope) {
                    dragTo(newOffsetForDelta(pixels))
                }
            }
        }

        override suspend fun drag(
            dragPriority: MutatePriority,
            block: suspend DragScope.() -> Unit
        ) {
            this@AnchoredDraggableState.anchoredDrag(dragPriority) {
                with(dragScope) { block() }
            }
        }

        override fun dispatchRawDelta(delta: Float) {
            this@AnchoredDraggableState.dispatchRawDelta(delta)
        }
    }

    var currentValue: T by mutableStateOf(initialValue)
        private set

    /**
     * The target value. This is the closest value to the current offset, taking into account
     * positional thresholds. If no interactions like animations or drags are in progress, this
     * will be the current value.
     */
    val targetValue: T by derivedStateOf {
        dragTarget ?: run {
            val currentOffset = offset
            if (!currentOffset.isNaN()) {
                computeTarget(currentOffset, currentValue, velocity = 0f)
            } else currentValue
        }
    }

    /**
     * The closest value in the swipe direction from the current offset, not considering thresholds.
     * If an [anchoredDrag] is in progress, this will be the target of that anchoredDrag (if
     * specified).
     */
    internal val closestValue: T by derivedStateOf {
        dragTarget ?: run {
            val currentOffset = offset
            if (!currentOffset.isNaN()) {
                computeTargetWithoutThresholds(currentOffset, currentValue)
            } else currentValue
        }
    }

    /**
     * The current offset, or [Float.NaN] if it has not been initialized yet.
     *
     * The offset will be initialized when the anchors are first set through [updateAnchors].
     *
     * Strongly consider using [requireOffset] which will throw if the offset is read before it is
     * initialized. This helps catch issues early in your workflow.
     */
    var offset: Float by mutableFloatStateOf(Float.NaN)
        private set

    /**
     * Require the current offset.
     *
     * @see offset
     *
     * @throws IllegalStateException If the offset has not been initialized yet
     */
    fun requireOffset(): Float {
        check(!offset.isNaN()) {
            "The offset was read before being initialized. Did you access the offset in a phase " +
                    "before layout, like effects or composition?"
        }
        return offset
    }

    /**
     * Whether an animation is currently in progress.
     */
    val isAnimationRunning: Boolean get() = dragTarget != null

    /**
     * The fraction of the progress going from [currentValue] to [closestValue], within [0f..1f]
     * bounds, or 1f if the [AnchoredDraggableState] is in a settled state.
     */
    @get:FloatRange(from = 0.0, to = 1.0)
    val progress: Float by derivedStateOf(structuralEqualityPolicy()) {
        val a = anchors.positionOf(currentValue)
        val b = anchors.positionOf(closestValue)
        val distance = abs(b - a)
        if (!distance.isNaN() && distance > 1e-6f) {
            val progress = (this.requireOffset() - a) / (b - a)
            // If we are very close to 0f or 1f, we round to the closest
            if (progress < 1e-6f) 0f else if (progress > 1 - 1e-6f) 1f else progress
        } else 1f
    }

    /**
     * The velocity of the last known animation. Gets reset to 0f when an animation completes
     * successfully, but does not get reset when an animation gets interrupted.
     * You can use this value to provide smooth reconciliation behavior when re-targeting an
     * animation.
     */
    var lastVelocity: Float by mutableFloatStateOf(0f)
        private set

    private var dragTarget: T? by mutableStateOf(null)

    var anchors: DraggableAnchors<T> by mutableStateOf(emptyDraggableAnchors())
        private set

    /**
     * Update the anchors. If there is no ongoing [anchoredDrag] operation, snap to the [newTarget],
     * otherwise restart the ongoing [anchoredDrag] operation (e.g. an animation) with the new
     * anchors.
     *
     * <b>If your anchors depend on the size of the layout, updateAnchors should be called in the
     * layout (placement) phase, e.g. through Modifier.onSizeChanged.</b> This ensures that the
     * state is set up within the same frame.
     * For static anchors, or anchors with different data dependencies, [updateAnchors] is safe to
     * be called from side effects or layout.
     *
     * @param newAnchors The new anchors.
     * @param newTarget The new target, by default the closest anchor or the current target if there
     * are no anchors.
     */
    fun updateAnchors(
        newAnchors: DraggableAnchors<T>,
        newTarget: T = if (!offset.isNaN()) {
            newAnchors.closestAnchor(offset) ?: targetValue
        } else targetValue
    ) {
        if (anchors != newAnchors) {
            anchors = newAnchors
            // Attempt to snap. If nobody is holding the lock, we can immediately update the offset.
            // If anybody is holding the lock, we send a signal to restart the ongoing work with the
            // updated anchors.
            val snapSuccessful = trySnapTo(newTarget)
            if (!snapSuccessful) {
                dragTarget = newTarget
            }
        }
    }

    /**
     * Find the closest anchor, taking into account the [velocityThreshold] and
     * [positionalThreshold], and settle at it with an animation.
     *
     * If the [velocity] is lower than the [velocityThreshold], the closest anchor by distance and
     * [positionalThreshold] will be the target. If the [velocity] is higher than the
     * [velocityThreshold], the [positionalThreshold] will <b>not</b> be considered and the next
     * anchor in the direction indicated by the sign of the [velocity] will be the target.
     */
    suspend fun settle(velocity: Float) {
        val previousValue = this.currentValue
        val targetValue = computeTarget(
            offset = requireOffset(),
            currentValue = previousValue,
            velocity = velocity
        )
        if (confirmValueChange(targetValue)) {
            animateTo(targetValue, velocity)
        } else {
            // If the user vetoed the state change, rollback to the previous state.
            animateTo(previousValue, velocity)
        }
    }

    private fun computeTarget(
        offset: Float,
        currentValue: T,
        velocity: Float
    ): T {
        val currentAnchors = anchors
        val currentAnchorPosition = currentAnchors.positionOf(currentValue)
        val velocityThresholdPx = velocityThreshold()
        return if (currentAnchorPosition == offset || currentAnchorPosition.isNaN()) {
            currentValue
        } else if (currentAnchorPosition < offset) {
            // Swiping from lower to upper (positive).
            if (velocity >= velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, true)!!
            } else {
                val upper = currentAnchors.closestAnchor(offset, true)!!
                val distance = abs(currentAnchors.positionOf(upper) - currentAnchorPosition)
                val relativeThreshold = abs(positionalThreshold(distance))
                val absoluteThreshold = abs(currentAnchorPosition + relativeThreshold)
                if (offset < absoluteThreshold) currentValue else upper
            }
        } else {
            // Swiping from upper to lower (negative).
            if (velocity <= -velocityThresholdPx) {
                currentAnchors.closestAnchor(offset, false)!!
            } else {
                val lower = currentAnchors.closestAnchor(offset, false)!!
                val distance = abs(currentAnchorPosition - currentAnchors.positionOf(lower))
                val relativeThreshold = abs(positionalThreshold(distance))
                val absoluteThreshold = abs(currentAnchorPosition - relativeThreshold)
                if (offset < 0) {
                    // For negative offsets, larger absolute thresholds are closer to lower anchors
                    // than smaller ones.
                    if (abs(offset) < absoluteThreshold) currentValue else lower
                } else {
                    if (offset > absoluteThreshold) currentValue else lower
                }
            }
        }
    }

    private fun computeTargetWithoutThresholds(
        offset: Float,
        currentValue: T,
    ): T {
        val currentAnchors = anchors
        val currentAnchorPosition = currentAnchors.positionOf(currentValue)
        return if (currentAnchorPosition == offset || currentAnchorPosition.isNaN()) {
            currentValue
        } else if (currentAnchorPosition < offset) {
            currentAnchors.closestAnchor(offset, true) ?: currentValue
        } else {
            currentAnchors.closestAnchor(offset, false) ?: currentValue
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    private val anchoredDragScope: AnchoredDragScope = object : AnchoredDragScope {
        override fun dragTo(newOffset: Float, lastKnownVelocity: Float) {
            offset = newOffset
            lastVelocity = lastKnownVelocity
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun anchoredDrag(
        dragPriority: MutatePriority = MutatePriority.Default,
        block: suspend AnchoredDragScope.(anchors: DraggableAnchors<T>) -> Unit
    ) {
        try {
            dragMutex.mutate(dragPriority) {
                restartable(inputs = { anchors }) { latestAnchors ->
                    anchoredDragScope.block(latestAnchors)
                }
            }
        } finally {
            val closest = anchors.closestAnchor(offset)
            if (closest != null &&
                abs(offset - anchors.positionOf(closest)) <= 0.5f &&
                confirmValueChange.invoke(closest)
            ) {
                currentValue = closest
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    suspend fun anchoredDrag(
        targetValue: T,
        dragPriority: MutatePriority = MutatePriority.Default,
        block: suspend AnchoredDragScope.(anchors: DraggableAnchors<T>, targetValue: T) -> Unit
    ) {
        if (anchors.hasAnchorFor(targetValue)) {
            try {
                dragMutex.mutate(dragPriority) {
                    dragTarget = targetValue
                    restartable(
                        inputs = { anchors to this@AnchoredDraggableState.targetValue }
                    ) { (latestAnchors, latestTarget) ->
                        anchoredDragScope.block(latestAnchors, latestTarget)
                    }
                }
            } finally {
                dragTarget = null
                val closest = anchors.closestAnchor(offset)
                if (closest != null &&
                    abs(offset - anchors.positionOf(closest)) <= 0.5f &&
                    confirmValueChange.invoke(closest)
                ) {
                    currentValue = closest
                }
            }
        } else {
            // Todo: b/283467401, revisit this behavior
            currentValue = targetValue
        }
    }

    internal fun newOffsetForDelta(delta: Float) =
        ((if (offset.isNaN()) 0f else offset) + delta)
            .coerceIn(anchors.minAnchor(), anchors.maxAnchor())

    /**
     * Drag by the [delta], coerce it in the bounds and dispatch it to the [AnchoredDraggableState].
     *
     * @return The delta the consumed by the [AnchoredDraggableState]
     */
    fun dispatchRawDelta(delta: Float): Float {
        val newOffset = newOffsetForDelta(delta)
        val oldOffset = if (offset.isNaN()) 0f else offset
        offset = newOffset
        return newOffset - oldOffset
    }

    /**
     * Attempt to snap synchronously. Snapping can happen synchronously when there is no other drag
     * transaction like a drag or an animation is progress. If there is another interaction in
     * progress, the suspending [snapTo] overload needs to be used.
     *
     * @return true if the synchronous snap was successful, or false if we couldn't snap synchronous
     */
    @OptIn(ExperimentalFoundationApi::class)
    private fun trySnapTo(targetValue: T): Boolean = dragMutex.tryMutate {
        with(anchoredDragScope) {
            val targetOffset = anchors.positionOf(targetValue)
            if (!targetOffset.isNaN()) {
                dragTo(targetOffset)
                dragTarget = null
            }
            currentValue = targetValue
        }
    }

    companion object {
        /**
         * The default [Saver] implementation for [AnchoredDraggableState].
         */
        @ExperimentalMaterialApi
        fun <T : Any> Saver(
            animationSpec: AnimationSpec<Float>,
            confirmValueChange: (T) -> Boolean,
            positionalThreshold: (distance: Float) -> Float,
            velocityThreshold: () -> Float,
        ) = Saver<AnchoredDraggableState<T>, T>(
            save = { it.currentValue },
            restore = {
                AnchoredDraggableState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    confirmValueChange = confirmValueChange,
                    positionalThreshold = positionalThreshold,
                    velocityThreshold = velocityThreshold
                )
            }
        )
    }
}
@ExperimentalFoundationApi
interface AnchoredDragScope {
    /**
     * Assign a new value for an offset value for [AnchoredDraggableState].
     *
     * @param newOffset new value for [AnchoredDraggableState.offset].
     * @param lastKnownVelocity last known velocity (if known)
     */
    fun dragTo(
        newOffset: Float,
        lastKnownVelocity: Float = 0f
    )
}
@Immutable
@kotlin.jvm.JvmInline
internal value class Strings private constructor(@Suppress("unused") private val value: Int) {
    companion object {
        val NavigationMenu = Strings(0)
        val CloseDrawer = Strings(1)
        val CloseSheet = Strings(2)
        val DefaultErrorMessage = Strings(3)
        val ExposedDropdownMenu = Strings(4)
        val SliderRangeStart = Strings(5)
        val SliderRangeEnd = Strings(6)
    }
}

@SuppressLint("PrivateResource")
@Composable
internal fun getString(string: Strings): String {
    LocalConfiguration.current
    val resources = LocalContext.current.resources
    return when (string) {
        Strings.NavigationMenu -> resources.getString(androidx.compose.ui.R.string.navigation_menu)
        Strings.CloseDrawer -> resources.getString(androidx.compose.ui.R.string.close_drawer)
        Strings.CloseSheet -> resources.getString(androidx.compose.ui.R.string.close_sheet)
        Strings.DefaultErrorMessage -> resources.getString(androidx.compose.ui.R.string.default_error_message)
        Strings.ExposedDropdownMenu -> resources.getString(androidx.compose.ui.R.string.dropdown_menu)
        Strings.SliderRangeStart -> resources.getString(androidx.compose.ui.R.string.range_start)
        Strings.SliderRangeEnd -> resources.getString(androidx.compose.ui.R.string.range_end)
        else -> ""
    }
}

@Composable
private fun Scrim(
    color: Color,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (color.isSpecified) {
        val alpha by animateFloatAsState(
            targetValue = if (visible) 1f else 0f,
            animationSpec = TweenSpec()
        )
        val closeSheet = getString(Strings.CloseSheet)
        val dismissModifier = if (visible) {
            Modifier
                .pointerInput(onDismiss) { detectTapGestures { onDismiss() } }
                .semantics(mergeDescendants = true) {
                    contentDescription = closeSheet
                    onClick { onDismiss(); true }
                }
        } else {
            Modifier
        }

        Canvas(
            Modifier
                .fillMaxSize()
                .then(dismissModifier)
        ) {
            drawRect(color = color, alpha = alpha)
        }
    }
}
@ExperimentalMaterialApi
internal class DraggableAnchorsConfig<T> {

    internal val anchors = mutableMapOf<T, Float>()

    /**
     * Set the anchor position for [this] anchor.
     *
     * @param position The anchor position.
     */
    @Suppress("BuilderSetStyle")
    infix fun T.at(position: Float) {
        anchors[this] = position
    }
}
@ExperimentalMaterialApi
internal fun <T : Any> DraggableAnchors(
    builder: com.software.jetpack.compose.chan_xin_android.ui.base.DraggableAnchorsConfig<T>.() -> Unit
): DraggableAnchors<T> = MapDraggableAnchors(
    DraggableAnchorsConfig<T>().apply(builder).anchors
)
@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.modalBottomSheetAnchors(
    sheetState: ModalBottomSheetState,
    fullHeight: Float
) = onSizeChanged { sheetSize ->
    val newAnchors = DraggableAnchors {
        ModalBottomSheetValue.Hidden at fullHeight
        val halfHeight = fullHeight / 2f
        if (!sheetState.isSkipHalfExpanded && sheetSize.height > halfHeight) {
            ModalBottomSheetValue.HalfExpanded at halfHeight
        }
        if (sheetSize.height != 0) {
            ModalBottomSheetValue.Expanded at max(0f, fullHeight - sheetSize.height)
        }
    }
    // If we are setting the anchors for the first time and have an anchor for
    // the current (initial) value, prefer that
    val isInitialized = sheetState.anchoredDraggableState.anchors.size > 0
    val previousValue = sheetState.currentValue
    val newTarget = if (!isInitialized && newAnchors.hasAnchorFor(previousValue)) {
        previousValue
    } else {
        when (sheetState.targetValue) {
            ModalBottomSheetValue.Hidden -> ModalBottomSheetValue.Hidden
            ModalBottomSheetValue.HalfExpanded, ModalBottomSheetValue.Expanded -> {
                val hasHalfExpandedState = newAnchors.hasAnchorFor(ModalBottomSheetValue.HalfExpanded)
                val newTarget = if (hasHalfExpandedState) {
                    ModalBottomSheetValue.HalfExpanded
                } else if (newAnchors.hasAnchorFor(ModalBottomSheetValue.Expanded)) {
                    ModalBottomSheetValue.Expanded
                } else {
                    ModalBottomSheetValue.Hidden
                }
                newTarget
            }
        }
    }
    sheetState.anchoredDraggableState.updateAnchors(newAnchors, newTarget)
}
@ExperimentalMaterialApi
internal fun <T> Modifier.anchoredDraggable(
    state: AnchoredDraggableState<T>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    startDragImmediately: Boolean = state.isAnimationRunning
) = draggable(
    state = state.draggableState,
    orientation = orientation,
    enabled = enabled,
    interactionSource = interactionSource,
    reverseDirection = reverseDirection,
    startDragImmediately = startDragImmediately,
    onDragStopped = { velocity -> launch { state.settle(velocity) } }
)
private val ModalBottomSheetPositionalThreshold = 56.dp
private val ModalBottomSheetVelocityThreshold = 125.dp
private val MaxModalBottomSheetWidth = 640.dp

@Stable
@ExperimentalMaterialApi
internal object AnchoredDraggableDefaults {
    /**
     * The default animation used by [AnchoredDraggableState].
     */
    @get:ExperimentalMaterialApi
    @Suppress("OPT_IN_MARKER_ON_WRONG_TARGET")
    @ExperimentalMaterialApi
    val AnimationSpec = SpringSpec<Float>()
}
@ExperimentalMaterialApi
enum class ModalBottomSheetValue {
    /**
     * The bottom sheet is not visible.
     */
    Hidden,

    /**
     * The bottom sheet is visible at full height.
     */
    Expanded,

    /**
     * The bottom sheet is partially visible at 50% of the screen height. This state is only
     * enabled if the height of the bottom sheet is more than 50% of the screen height.
     */
    HalfExpanded
}
@ExperimentalMaterialApi
@Suppress("Deprecation")
fun ModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    density: Density,
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    confirmValueChange: (ModalBottomSheetValue) -> Boolean = { true },
    isSkipHalfExpanded: Boolean = false,
) = ModalBottomSheetState(
    initialValue = initialValue,
    animationSpec = animationSpec,
    isSkipHalfExpanded = isSkipHalfExpanded,
    confirmStateChange = confirmValueChange
).also {
    it.density = density
}
@ExperimentalMaterialApi
class ModalBottomSheetState @Deprecated(
    message = "This constructor is deprecated. confirmStateChange has been renamed to " +
            "confirmValueChange.",
    replaceWith = ReplaceWith(
        "ModalBottomSheetState(" +
                "initialValue, animationSpec, confirmStateChange, isSkipHalfExpanded)"
    )
) constructor(
    initialValue: ModalBottomSheetValue,
    internal val animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    internal val isSkipHalfExpanded: Boolean = false,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean
) {

    internal val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        animationSpec = animationSpec,
        confirmValueChange = confirmStateChange,
        positionalThreshold = {
            with(requireDensity()) {
                ModalBottomSheetPositionalThreshold.toPx()
            }
        },
        velocityThreshold = { with(requireDensity()) { ModalBottomSheetVelocityThreshold.toPx() } }
    )

    /**
     * The current value of the [ModalBottomSheetState].
     */
    val currentValue: ModalBottomSheetValue
        get() = anchoredDraggableState.currentValue

    /**
     * The target value the state will settle at once the current interaction ends, or the
     * [currentValue] if there is no interaction in progress.
     */
    val targetValue: ModalBottomSheetValue
        get() = anchoredDraggableState.targetValue

    /**
     * The fraction of the progress, within [0f..1f] bounds, or 1f if the [AnchoredDraggableState]
     * is in a settled state.
     */
    @get:FloatRange(from = 0.0, to = 1.0)
    @ExperimentalMaterialApi
    val progress: Float
        get() = anchoredDraggableState.progress

    /**
     * Whether the bottom sheet is visible.
     */
    val isVisible: Boolean
        get() = anchoredDraggableState.currentValue != ModalBottomSheetValue.Hidden

    internal val hasHalfExpandedState: Boolean
        get() = anchoredDraggableState.anchors.hasAnchorFor(ModalBottomSheetValue.HalfExpanded)

    @Deprecated(
        message = "This constructor is deprecated. confirmStateChange has been renamed to " +
                "confirmValueChange.",
        replaceWith = ReplaceWith(
            "ModalBottomSheetState(" +
                    "initialValue, animationSpec, confirmStateChange, false)"
        )
    )
    @Suppress("Deprecation")
    constructor(
        initialValue: ModalBottomSheetValue,
        animationSpec: AnimationSpec<Float>,
        confirmStateChange: (ModalBottomSheetValue) -> Boolean
    ) : this(initialValue, animationSpec, isSkipHalfExpanded = false, confirmStateChange)

    init {
        if (isSkipHalfExpanded) {
            require(initialValue != ModalBottomSheetValue.HalfExpanded) {
                "The initial value must not be set to HalfExpanded if skipHalfExpanded is set to" +
                        " true."
            }
        }
    }

    /**
     * Show the bottom sheet with animation and suspend until it's shown. If the sheet is taller
     * than 50% of the parent's height, the bottom sheet will be half expanded. Otherwise it will be
     * fully expanded.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun show() {
        val targetValue = when {
            hasHalfExpandedState -> ModalBottomSheetValue.HalfExpanded
            else -> ModalBottomSheetValue.Expanded
        }
        animateTo(targetValue)
    }

    /**
     * Half expand the bottom sheet if half expand is enabled with animation and suspend until it
     * animation is complete or cancelled
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    internal suspend fun halfExpand() {
        if (!hasHalfExpandedState) {
            return
        }
        animateTo(ModalBottomSheetValue.HalfExpanded)
    }

    /**
     * Fully expand the bottom sheet with animation and suspend until it if fully expanded or
     * animation has been cancelled.
     * *
     * @throws [CancellationException] if the animation is interrupted
     */
    internal suspend fun expand() {
        if (!anchoredDraggableState.anchors.hasAnchorFor(ModalBottomSheetValue.Expanded)) {
            return
        }
        animateTo(ModalBottomSheetValue.Expanded)
    }

    /**
     * Hide the bottom sheet with animation and suspend until it if fully hidden or animation has
     * been cancelled.
     *
     * @throws [CancellationException] if the animation is interrupted
     */
    suspend fun hide() = animateTo(ModalBottomSheetValue.Hidden)

    internal suspend fun animateTo(
        target: ModalBottomSheetValue,
        velocity: Float = anchoredDraggableState.lastVelocity
    ) = anchoredDraggableState.animateTo(target, velocity)

    internal suspend fun snapTo(target: ModalBottomSheetValue) =
        anchoredDraggableState.snapTo(target)

    internal fun requireOffset() = anchoredDraggableState.requireOffset()

    internal var density: Density? = null
    private fun requireDensity() = requireNotNull(density) {
        "The density on ModalBottomSheetState ($this) was not set. Did you use " +
                "ModalBottomSheetState with the ModalBottomSheetLayout composable?"
    }

    companion object {
        /**
         * The default [Saver] implementation for [ModalBottomSheetState].
         * Saves the [currentValue] and recreates a [ModalBottomSheetState] with the saved value as
         * initial value.
         */
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmValueChange: (ModalBottomSheetValue) -> Boolean,
            skipHalfExpanded: Boolean,
            density: Density
        ): Saver<ModalBottomSheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                ModalBottomSheetState(
                    initialValue = it,
                    density = density,
                    animationSpec = animationSpec,
                    isSkipHalfExpanded = skipHalfExpanded,
                    confirmValueChange = confirmValueChange
                )
            }
        )

        /**
         * The default [Saver] implementation for [ModalBottomSheetState].
         * Saves the [currentValue] and recreates a [ModalBottomSheetState] with the saved value as
         * initial value.
         */
        @Deprecated(
            message = "This function is deprecated. Please use the overload where Density is" +
                    " provided.",
            replaceWith = ReplaceWith(
                "Saver(animationSpec, confirmValueChange, density, " +
                        "skipHalfExpanded)"
            )
        )
        @Suppress("Deprecation")
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmValueChange: (ModalBottomSheetValue) -> Boolean,
            skipHalfExpanded: Boolean,
        ): Saver<ModalBottomSheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                ModalBottomSheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    isSkipHalfExpanded = skipHalfExpanded,
                    confirmValueChange = confirmValueChange
                )
            }
        )

        /**
         * The default [Saver] implementation for [ModalBottomSheetState].
         * Saves the [currentValue] and recreates a [ModalBottomSheetState] with the saved value as
         * initial value.
         */
        @Deprecated(
            message = "This function is deprecated. confirmStateChange has been renamed to " +
                    "confirmValueChange.",
            replaceWith = ReplaceWith(
                "Saver(animationSpec, confirmStateChange, " +
                        "skipHalfExpanded)"
            )
        )
        @Suppress("Deprecation")
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            skipHalfExpanded: Boolean,
            confirmStateChange: (ModalBottomSheetValue) -> Boolean
        ): Saver<ModalBottomSheetState, *> = Saver(
            animationSpec = animationSpec,
            confirmValueChange = confirmStateChange,
            skipHalfExpanded = skipHalfExpanded
        )
    }
}
object ModalBottomSheetDefaults {

    /**
     * The default elevation used by [ModalBottomSheetLayout].
     */
    val Elevation = 16.dp

    /**
     * The default scrim color used by [ModalBottomSheetLayout].
     */
    val scrimColor: Color
        @Composable
        get() = MaterialTheme.colors.onSurface.copy(alpha = 0.32f)
}
@Composable
@ExperimentalMaterialApi
fun ModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState =
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    onDismiss: () -> Unit = {},
    sheetGesturesEnabled: Boolean = true,
    sheetShape: Shape = MaterialTheme.shapes.large,
    sheetElevation: Dp = ModalBottomSheetDefaults.Elevation,
    sheetBackgroundColor: Color = MaterialTheme.colors.surface,
    sheetContentColor: Color = contentColorFor(sheetBackgroundColor),
    scrimColor: Color = ModalBottomSheetDefaults.scrimColor,
    content: @Composable () -> Unit
) {
    // b/278692145 Remove this once deprecated methods without density are removed
    val density = LocalDensity.current
    SideEffect {
        sheetState
    }
    val scope = rememberCoroutineScope()
    val orientation = Orientation.Vertical
    BoxWithConstraints(modifier) {
        val fullHeight = constraints.maxHeight.toFloat()
        Box(Modifier.fillMaxSize()) {
            content()
            Scrim(
                color = scrimColor,
                onDismiss = {
                    if (sheetState.anchoredDraggableState.confirmValueChange(ModalBottomSheetValue.Hidden)) {
                        scope.launch { sheetState.hide() }
                        onDismiss()
                    }
                },
                visible = sheetState.anchoredDraggableState.targetValue != ModalBottomSheetValue.Hidden
            )
        }
        Surface(
            Modifier
                .align(Alignment.TopCenter) // We offset from the top so we'll center from there
                .widthIn(max = MaxModalBottomSheetWidth)
                .fillMaxWidth()
                .then(
                    if (sheetGesturesEnabled) {
                        Modifier.nestedScroll(
                            remember(sheetState.anchoredDraggableState, orientation) {
                                ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
                                    state = sheetState.anchoredDraggableState,
                                    orientation = orientation
                                )
                            }
                        )
                    } else Modifier
                )
                .offset {
                    IntOffset(
                        0,
                        sheetState.anchoredDraggableState
                            .requireOffset()
                            .roundToInt()
                    )
                }
                .anchoredDraggable(
                    state = sheetState.anchoredDraggableState,
                    orientation = orientation,
                    enabled = sheetGesturesEnabled &&
                            sheetState.anchoredDraggableState.currentValue != ModalBottomSheetValue.Hidden,
                )
                .modalBottomSheetAnchors(sheetState, fullHeight)
                .then(
                    if (sheetGesturesEnabled) {
                        Modifier.semantics {
                            if (sheetState.isVisible) {
                                dismiss {
                                    if (
                                        sheetState.anchoredDraggableState.confirmValueChange(ModalBottomSheetValue.Hidden)
                                    ) {
                                        scope.launch { sheetState.hide() }
                                    }
                                    true
                                }
                                if (sheetState.anchoredDraggableState.currentValue
                                    == ModalBottomSheetValue.HalfExpanded
                                ) {
                                    expand {
                                        if (sheetState.anchoredDraggableState.confirmValueChange(
                                                ModalBottomSheetValue.Expanded
                                            )
                                        ) {
                                            scope.launch { sheetState.expand() }
                                        }
                                        true
                                    }
                                } else if (sheetState.hasHalfExpandedState) {
                                    collapse {
                                        if (sheetState.anchoredDraggableState.confirmValueChange(
                                                ModalBottomSheetValue.HalfExpanded
                                            )
                                        ) {
                                            scope.launch { sheetState.halfExpand() }
                                        }
                                        true
                                    }
                                }
                            }
                        }
                    } else Modifier
                ),
            shape = sheetShape,
            elevation = sheetElevation,
            color = sheetBackgroundColor,
            contentColor = sheetContentColor
        ) {
            Column(content = sheetContent)
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
private fun ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection(
    state: AnchoredDraggableState<*>,
    orientation: Orientation
): NestedScrollConnection = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.Drag) {
            state.dispatchRawDelta(delta).toOffset()
        } else {
            Offset.Zero
        }
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        return if (source == NestedScrollSource.Drag) {
            state.dispatchRawDelta(available.toFloat()).toOffset()
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = available.toFloat()
        val currentOffset = state.requireOffset()
        return if (toFling < 0 && currentOffset > state.anchors.minAnchor()) {
            state.settle(velocity = toFling)
            // since we go to the anchor with tween settling, consume all for the best UX
            available
        } else {
            Velocity.Zero
        }
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        state.settle(velocity = available.toFloat())
        return available
    }

    private fun Float.toOffset(): Offset = Offset(
        x = if (orientation == Orientation.Horizontal) this else 0f,
        y = if (orientation == Orientation.Vertical) this else 0f
    )

    @JvmName("velocityToFloat")
    private fun Velocity.toFloat() = if (orientation == Orientation.Horizontal) x else y

    @JvmName("offsetToFloat")
    private fun Offset.toFloat(): Float = if (orientation == Orientation.Horizontal) x else y
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("Deprecation")
fun ModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = AnchoredDraggableDefaults.AnimationSpec,
    confirmValueChange: (ModalBottomSheetValue) -> Boolean = { true },
    isSkipHalfExpanded: Boolean = false
) = ModalBottomSheetState(
    initialValue = initialValue,
    animationSpec = animationSpec,
    isSkipHalfExpanded = isSkipHalfExpanded,
    confirmStateChange = confirmValueChange
)