package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.data.repository.FitLifeRepository
import com.example.ui.screens.YogaScreen
import com.example.ui.theme.FitLifeIndiaTheme

class YogaFragment : Fragment() {
    var repository: FitLifeRepository? = null

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
                    YogaScreen(repository = repo)
                }
            }
        }
    }

    companion object {
        fun newInstance(title: String = "Yoga"): YogaFragment {
            return YogaFragment().apply {
                arguments = Bundle().apply {
                    putString(FitLifeBaseFragment.ARG_SCREEN_TITLE, title)
                }
            }
        }
    }
}
