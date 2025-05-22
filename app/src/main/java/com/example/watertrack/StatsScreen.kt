package com.example.watertrack

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.watertrack.data.WaterDataRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
@Composable
fun StatsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val waterRepository = remember { WaterDataRepository(context) }

    val waterHistory by waterRepository.getAllWaterIntakeHistory().collectAsState(initial = emptyMap())

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Water Intake History",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (waterHistory.isEmpty()) {
            Text(
                "No data yet. Start tracking your water intake!",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            val P_displayList = waterHistory.toList()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(P_displayList) { (dateString, intake) ->
                    val P_displayDate = try {
                        LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
                            .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                    } catch (e: Exception) {
                        dateString
                    }
                    WaterHistoryItem(date = P_displayDate, intake = intake.toInt())
                }
            }
        }
    }
}

@Composable
fun WaterHistoryItem(date: String, intake: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = date, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "${intake}ml",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}