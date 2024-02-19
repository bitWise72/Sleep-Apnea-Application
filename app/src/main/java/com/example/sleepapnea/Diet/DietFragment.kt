package com.example.sleepapnea.Diet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.view.View
import android.view.ViewGroup
import com.example.sleepapnea.R

<RelativeLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/black"
    android:background="@color/white"
    android:background="@color/Main_theme"
    android:background="@color/Color_theme">
</RelativeLayout>

<TextView
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="Sample Text"
android:fontFamily="@font"/>


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DietFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DietFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button in your layout
        val generateTestButton: Button = view.findViewById(R.id.generateTestButton)

        // Set click listener to the button
        generateTestButton.setOnClickListener {
            showQuestionDialog()
        }
    }

    private fun showQuestionDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_question, null)
        val questionTextView = dialogView.findViewById<TextView>(R.id.questionTextView)
        val answerEditText = dialogView.findViewById<EditText>(R.id.answerEditText)

        // questions
        val questions = arrayOf(
            "Do you have hypertension?",
            "Do you experience daytime sleepiness?"
        )

        // Initialize current question index
        var currentQuestionIndex = 0

        // Display the first question
        questionTextView.text = questions[currentQuestionIndex]

        builder.setView(dialogView)
            .setPositiveButton("Next") { dialog, _ ->
                // Move to the next question
                currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                questionTextView.text = questions[currentQuestionIndex]
            }
            .setNegativeButton("Previous") { dialog, _ ->
                // Move to the previous question
                currentQuestionIndex = (currentQuestionIndex - 1 + questions.size) % questions.size
                questionTextView.text = questions[currentQuestionIndex]
            }
            .create()
            .show()
        yesButton.setOnClickListener {
            // Handle yes answer
            // You can save the answer or perform any other action here
        }

        noButton.setOnClickListener {
            // Handle no answer
            // You can save the answer or perform any other action here
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DietFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DietFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}