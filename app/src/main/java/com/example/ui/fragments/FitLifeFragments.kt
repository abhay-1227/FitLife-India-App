package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.data.repository.FitLifeRepository
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.FitLifeIndiaTheme

open class FitLifeBaseFragment : Fragment() {
    protected var screenTitle: String = "FitLife"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenTitle = arguments?.getString(ARG_SCREEN_TITLE) ?: "FitLife"
    }

    companion object {
        const val ARG_SCREEN_TITLE = "arg_screen_title"
    }
}

class VitalityFragment : FitLifeBaseFragment() {
    var repository: FitLifeRepository? = null
    var onNavigateToNutrition: (() -> Unit)? = null
    var onNavigateToYoga: (() -> Unit)? = null
    var onNavigateToTerms: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val repo = repository ?: return@setContent
                FitLifeIndiaTheme(themeMode = repo.preferences.themeMode) {
                    HomeScreen(
                        repository = repo,
                        onNavigateToNutrition = { onNavigateToNutrition?.invoke() },
                        onNavigateToYoga = { onNavigateToYoga?.invoke() },
                        onNavigateToTerms = { onNavigateToTerms?.invoke() }
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(title: String = "Dashboard"): VitalityFragment {
            return VitalityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCREEN_TITLE, title)
                }
            }
        }
    }
}
