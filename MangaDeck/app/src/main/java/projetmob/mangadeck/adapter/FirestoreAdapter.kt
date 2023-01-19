package projetmob.mangadeck.adapter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import java.util.ArrayList

/**
 * adapter essentiel pour la communication entre les adapters et firebase
 */
abstract class FirestoreAdapter<VH : RecyclerView.ViewHolder>(
    private val query: Query
) : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()

    /**
     * permet de commencer a écouter
     */
    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }
    /**
     * permet d'arreter d' écouter
     */
    open fun stopListening() {
        if (registration != null) {
            registration!!.remove()
            registration = null
        }

        snapshots.clear()
    }

    override fun onEvent(
        documentSnapshots: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        // Handle errors
        if (exception != null) {
            Log.e("onEvent:error", exception.toString())
            return
        }

        // Dispatch the event
        for (change in documentSnapshots!!.documentChanges) {
            // Snapshot of the changed document
            when (change.type) {
                DocumentChange.Type.ADDED -> onDocumentAdded(change)
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
    }

    protected open fun onDocumentAdded(change: DocumentChange) {
        snapshots.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    protected open fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    protected open fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected open fun getSnapshot(index: Int): DocumentSnapshot? {
        return snapshots[index]
    }
}