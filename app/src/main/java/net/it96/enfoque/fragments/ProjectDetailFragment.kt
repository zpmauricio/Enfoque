package net.it96.enfoque.fragments

//import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_project_detail.view.*
import net.it96.enfoque.R
import net.it96.enfoque.database.Project
import timber.log.Timber

class ProjectDetailFragment : Fragment() {

    private lateinit var selectedProject : Project

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_project_detail, container, false)

        Timber.i("***MZP*** onCreateView")
        requireArguments().let {
//            Timber.i("***MZP*** requireArguments")
//            Timber.i("***MZP*** it: ${it}")
//            Timber.i("***MZP*** it: ${it.getParcelable<Project>("Project")!!.name}")
            view.txt_projectName.text = it.getParcelable<Project>("Project")!!.name
            selectedProject = it.getParcelable("Project")!!
//            Timber.i("Detail Frag: $selectedProject")
        }

//        NinetyDayGoals()

        view.btn_Obj90Days.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Project", selectedProject)
            findNavController().navigate(R.id.action_projectDetailFragment_to_ninetyDayGoalsFragment, bundle)
        }

        return view
    }

//    private fun NinetyDayGoals(){
//        btn_Obj90Days.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putParcelable("Project", selectedProject)
//            findNavController().navigate(R.id.action_projectDetailFragment_to_ninetyDayGoalsFragment)
//        }
//    }
}