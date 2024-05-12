package com.example.listit.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.listit.MainActivity
import com.example.listit.R
import com.example.listit.databinding.FragmentEditTaskBinding
import com.example.listit.model.Task
import com.example.listit.viewmodel.TaskViewModel


class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editTaskTitle.setText(currentTask.taskTitle)
        binding.editTaskDescription.setText(currentTask.taskDescription)
        binding.editTaskDeadline.setText(currentTask.taskDeadline)

        val priorities = resources.getStringArray(R.array.priority_levels)
        val priorityIndex = priorities.indexOf(currentTask.taskPriority)
        binding.editTaskPriority.setSelection(priorityIndex)

        binding.editTaskFab.setOnClickListener {
            val taskTitle = binding.editTaskTitle.text.toString().trim()
            val taskDescription = binding.editTaskDescription.text.toString().trim()
            val taskPriority = binding.editTaskPriority.selectedItem.toString().trim()
            val taskDeadline = binding.editTaskDeadline.text.toString().trim()

            if (taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDescription, taskPriority, taskDeadline)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTask() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Task")
            setMessage("Are you sure you want to delete this task?")
            setPositiveButton("Delete") { _, _ ->
                taskViewModel.deleteTask(currentTask)
                Toast.makeText(context, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                view?.findNavController()?.popBackStack(R.id.homeFragment, false)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_task, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteTask()
                true
            } else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }

}