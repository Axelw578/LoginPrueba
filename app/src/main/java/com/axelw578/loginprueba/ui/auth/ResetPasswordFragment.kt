package com.axelw578.loginprueba.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.axelw578.loginprueba.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentResetPasswordBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnResetPassword.setOnClickListener {
            val email = binding.etResetEmail.text.toString().trim()
            if (email.isNotEmpty()) {
                authViewModel.resetPassword(email) { success ->
                    if (success) {
                        // Mostrar mensaje de confirmación
                        Toast.makeText(
                            requireContext(),
                            "Correo enviado, revisa tu bandeja de entrada",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Aquí podrías navegar de vuelta al Login o a otra pantalla, según convenga.
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al enviar el correo. Intenta de nuevo",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
