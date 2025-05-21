package com.example.watertrack

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val goal = 2000
    var currentProgress by remember {
        mutableStateOf(0f)
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Today's progress",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayMedium // Use typography styles
            )
            Text(
                "${currentProgress.toInt()}ml / ${goal}ml",
                style = MaterialTheme.typography.displaySmall
            )

            // Show overachievement message when reaching or exceeding the goal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentProgress >= goal) {
                    Text(
                        "Great job! You've ${if (currentProgress > goal) "exceeded" else "reached"} your daily goal!",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Water level indicator with properly drawn border
        FillingCircleWithBorder(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(240.dp)
                .padding(16.dp),
            progress = (currentProgress / goal).coerceIn(0f, 1f),
            currentProgress = currentProgress,
            dailyGoal = goal,
            exceededGoalColor = MaterialTheme.colorScheme.tertiary,
            borderColor = MaterialTheme.colorScheme.primary,
            fillColor = MaterialTheme.colorScheme.primaryContainer,
            backgroundColor = MaterialTheme.colorScheme.background,
            textColor = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Water intake buttons section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Add water intake",
                style = MaterialTheme.typography.titleMedium
            )

            // Row of add buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WaterButton(
                    text = "+100ml",
                    onClick = { currentProgress += 100f }
                )

                WaterButton(
                    text = "+250ml",
                    onClick = { currentProgress += 250f }
                )

                WaterButton(
                    text = "+500ml",
                    onClick = { currentProgress += 500f }
                )
            }

            // Second row with custom button and remove water option
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WaterButton(
                    text = "Full Cup\n(330ml)",
                    onClick = { currentProgress += 330f },

                )

                // Remove water button with different color
                WaterButton(
                    text = "-100ml",
                    onClick = { currentProgress = (currentProgress - 100f).coerceAtLeast(0f) },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}


@Composable
fun WaterButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    height: Dp = 50.dp // Added height parameter with default value
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier
            .height(height) // Use the provided height parameter
            .width(110.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

// Update FillingCircleWithBorder to use typography styles
@Composable
fun FillingCircleWithBorder(
    progress: Float,
    currentProgress: Float,
    dailyGoal: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    fillColor: Color,
    borderColor: Color,
    exceededGoalColor: Color,
    textColor: Color,
    icon: Int = R.drawable.droplet
) {
    val animatedProgress by animateFloatAsState(targetValue = progress)
    val goalReached = currentProgress >= dailyGoal

    val circleBorderColor = if (goalReached) exceededGoalColor else borderColor
    val circleFillColor = if (goalReached) exceededGoalColor.copy(alpha = 0.7f) else fillColor
    val circleTextColor = if (goalReached) exceededGoalColor else textColor

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Canvas code remains the same
            val size = size.minDimension
            val borderWidth = 8f
            val radius = (size / 2f) - (borderWidth / 2f)
            val center = Offset(this.size.width / 2f, this.size.height / 2f)
            val fillHeight = 2 * radius * animatedProgress
            val circlePath = Path().apply {
                addOval(Rect(center = center, radius = radius))
            }

            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = center
            )

            clipPath(circlePath) {
                drawRect(
                    color = circleFillColor,
                    topLeft = Offset(center.x - radius, center.y + radius - fillHeight),
                    size = Size(width = 2 * radius, height = fillHeight)
                )
            }

            drawCircle(
                color = circleBorderColor,
                radius = radius,
                center = center,
                style = Stroke(width = borderWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Drop Icon",
                tint = circleTextColor,
                modifier = Modifier.size(36.dp)
            )

            Text(
                text = if (goalReached) "AMAZING!" else "${(animatedProgress * 100).toInt()}%",
                color = circleTextColor,
                style = if (goalReached) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleLarge
            )

            Box(
                modifier = Modifier.height(20.dp),
                contentAlignment = Alignment.Center
            ) {
                if (goalReached) {
                    Text(
                        text = "${(currentProgress / dailyGoal * 100).toInt()}%",
                        color = circleTextColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
