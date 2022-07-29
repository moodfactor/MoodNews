package com.mood.moodnews.interests

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.mood.moodnews.R

enum class Sections(@StringRes val titleResId: Int) {
    Topics(R.string.interests_section_topics),
    People(R.string.interests_section_people),
    Publications(R.string.interests_section_publications)
}

/**
 * TabContent for a single tab of the screen.
 *
 * This is intended to encapsulate a tab & it's content as a single object. It was added to avoid
 * passing several parameters per-tab from the stateful composable to the composable that displays
 * the current tab.
 *
 * @param section the tab that this content is for
 * @param section content of the tab, a composable that describes the content
 */
class TabContent(val section: Sections, val content: @Composable () -> Unit)


@Composable
private fun InterestsTabRowContent(
    selectedTabIndex: Int,
    updateSectin: (Sections) -> Int,
    tabContent: List<TabContent>,
    modifier: Modifier = Modifier
) {
    tabContent.forEachIndexed { index, content ->
        val colorText = if (selectedTabIndex == index) {

        }
    }
}

/**
 * Custom layout for the Interests Screen that places items on the screen given the available size.
 *
 * For example: Given a list of items (A, B, C, D, E) and a screen size that allows only 2 columns,
 * the items will be displayed on the sceen as follows:
 * A B
 * C D
 * E
 */

@Composable
private fun InterestsAdaptiveContentLayout(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    itemSpacing: Dp = 4.dp,
    itemMaxWidth: Dp = 450.dp,
    multipleColumnsBreakPoint: Dp = 600.dp,
    content: @Composable () -> Unit,

    ) {
    Layout(modifier = modifier, content = content) { measurables, outerConstraints ->
        // Convert parameters to Px. Safe to do as `Layout` measure block runs in a `Density` scope
        val multipleColumnsBreakPointPx = multipleColumnsBreakPoint.roundToPx()
        val topPaddingPx = topPadding.roundToPx()
        val itemSpacingPx = itemSpacing.roundToPx()
        val itemMaxWidthPx = itemMaxWidth.roundToPx()

        // Number of columns to display on the screen. This is harcoded to 2 due to
        // the design mocks, but this logic could change in the future.
        val columns = if (outerConstraints.maxWidth < multipleColumnsBreakPointPx) 1 else 2
        // Max width for each item taking into account available space, spacing and `itemMaxWidth`
        val itemWidth = if (columns == 1) {
            outerConstraints.maxWidth
        } else {
            val maxWidthWithSpaces = outerConstraints.maxWidth - (columns - 1) * itemSpacingPx
            (maxWidthWithSpaces / columns).coerceIn(0, itemMaxWidthPx)
        }
        val itemConstraints = outerConstraints.copy(maxWidth = itemWidth)

        // Keep track of the height of each row to calculate the layout's final size
        val rowHeights = IntArray(measurables.size / columns + 1)
        // Measure elements with their maximum width and keep track of the height
        val placeables = measurables.mapIndexed { index, measureable ->
            val placeable = measureable.measure(itemConstraints)
            // Update the height for each row
            val row = index.floorDiv(columns)
            rowHeights[row] = kotlin.math.max(rowHeights[row], placeable.height)
            placeable
        }

        // Calculate maxHeight of the Interests layout. Heights of the row + top padding
        val layoutHeight = topPaddingPx + rowHeights.sum()
        // Calculate maxWidth of the Interests layout
        val layoutWidth = itemWidth * columns + (itemSpacingPx * (columns - 1))

        // Lay out given the max width and height
        layout(
            width = outerConstraints.constrainWidth(layoutWidth),
            height = outerConstraints.constrainHeight(layoutHeight)
        ) {
            // Track the y co-ord we have placed children up to
            var yPosition = topPaddingPx
            // Split placeables in lists that don't exceed the number of columns
            // and place them taking into account their width and spacing
            placeables.chunked(columns).forEachIndexed { rowIndex, row ->
                var xPosition = 0
                row.forEach { placeable ->
                    placeable.placeRelative(x = xPosition, y = yPosition)
                    xPosition += placeable.width + itemSpacingPx
                }
                yPosition += rowHeights[rowIndex]
            }
        }
    }
}