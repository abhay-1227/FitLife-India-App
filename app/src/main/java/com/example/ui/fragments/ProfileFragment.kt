package com.example.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.example.data.repository.FitLifeRepository
import com.example.ui.screens.ProfileScreen
import com.example.ui.theme.FitLifeIndiaTheme

class ProfileFragment : Fragment() {
    var repository: FitLifeRepository? = null
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
                    ProfileScreen(
                        repository = repo,
                        onNavigateToTerms = { onNavigateToTerms?.invoke() }
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(title: String = "Profile"): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(FitLifeBaseFragment.ARG_SCREEN_TITLE, title)
                }
            }
        }
    }
}
