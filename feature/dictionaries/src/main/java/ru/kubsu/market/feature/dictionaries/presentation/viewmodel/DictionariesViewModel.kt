package ru.kubsu.market.feature.dictionaries.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.kubsu.market.core.model.*
import ru.kubsu.market.core.ui.mapping.toUiDisplayable
import ru.kubsu.market.feature.dictionaries.domain.usecase.GetDictionaryItemsUseCase
import ru.kubsu.market.feature.dictionaries.presentation.model.DictionariesUiEvent
import ru.kubsu.market.feature.dictionaries.presentation.model.DictionariesUiState
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DictionariesViewModel @Inject constructor(
    private val getDictionaryItemsUseCase: GetDictionaryItemsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<DictionariesUiState>(DictionariesUiState.Idle)
    val uiState: StateFlow<DictionariesUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<DictionariesUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<String>(KEY_SELECTED_ENDPOINT)?.let { endpoint ->
            val template = getDictionaryItemTemplateByEndpoint(endpoint)
            if (template != null) {
                getDictionaryItems(template)
            }
        }
    }

    fun getDictionaryItems(item: IDictionaryItem) {
        _uiState.value = DictionariesUiState.Loading
        savedStateHandle[KEY_SELECTED_ENDPOINT] = item.endpoint
        viewModelScope.launch {
            try {
                val list = getDictionaryItemsUseCase(item)
                val uiList = list.map { dictItem ->
                    when (dictItem) {
                        is ContactPerson -> dictItem.toUiDisplayable()
                        is Counterparty -> dictItem.toUiDisplayable()
                        is SupplyContract -> dictItem.toUiDisplayable()
                        is SupplyContractItem -> dictItem.toUiDisplayable()
                        is Truck -> dictItem.toUiDisplayable()
                        else -> throw IllegalArgumentException("Unknown dictionary item type")
                    }
                }
                _uiState.value = DictionariesUiState.Success(uiList, item.className)
            } catch (e: Exception) {
                val errorMessage = e.message ?: "Ошибка загрузки справочника"
                _uiState.value = DictionariesUiState.Error(errorMessage)
                _uiEvent.send(DictionariesUiEvent.ShowToast(errorMessage))
            }
        }
    }

    fun reset() {
        _uiState.value = DictionariesUiState.Idle
        savedStateHandle[KEY_SELECTED_ENDPOINT] = null
    }

    private fun getDictionaryItemTemplateByEndpoint(endpoint: String): IDictionaryItem? {
        return when (endpoint) {
            "contact-persons" -> ContactPerson(counterpartyId = 0, phone = "", email = "", fullName = "")
            "counterparties" -> Counterparty(name = "", address = "", contactInfo = "")
            "supply-contracts" -> SupplyContract(contractorId = -1, storageLocationId = -1, startDate = LocalDate.now(), endDate = LocalDate.now())
            "supply-contract-items" -> SupplyContractItem(supplyContractId = -1, productId = -1, deliveryType = "")
            "trucks" -> Truck(licencePlate = "", model = "", capacity = BigDecimal.ONE, driverId = -1)
            else -> null
        }
    }

    companion object {
        private const val KEY_SELECTED_ENDPOINT = "selected_endpoint"
    }
}
