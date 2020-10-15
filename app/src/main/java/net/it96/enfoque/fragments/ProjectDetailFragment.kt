package net.it96.enfoque.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            Timber.i("***MZP*** requireArguments")
            Timber.i("***MZP*** it: ${it}")
            Timber.i("***MZP*** it: ${it.getParcelable<Project>("Project")!!.name}")
            view.etxt_projectName.text = it.getParcelable<Project>("Project")!!.name
            selectedProject = it.getParcelable("Project")!!
            Timber.i("Detail Frag: $selectedProject")
        }

        return view
    }
}