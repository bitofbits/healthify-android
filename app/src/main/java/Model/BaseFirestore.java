package Model;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;

public abstract class BaseFirestore
{

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public abstract String getID();
    public void sendToFirestore()
    {
        String collection_name = this.getClass().getSimpleName();
        String document_id = this.getID();
        //int i=0;

        DocumentReference docref = db.collection(collection_name).document(document_id);
//        docref.set(this).addOnSuccessListener(new OnSuccessListener<Void>()
//        {
//            @Override
//            public void onSuccess(Void aVoid)
//            {
//                //Toast.makeText(this,"User exists with password :"+pa, Toast.LENGTH_SHORT).show();
//                System.out.println("Added Succesfuly data to db---------------------");
//            }
//        }).addOnFailureListener(new OnFailureListener()
//        {
//            @Override
//            public void onFailure(@NonNull Exception e)
//            {
//                System.out.println("Could not add the object: "+this+ " of the class: "+this.getClass()+" to the database");
//                System.out.println("Exception occured: "+e);
//            }
//        });

        docref.set(this).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    System.out.println("Added data properly");
                }
                else
                {
                    System.out.println("Error in uploading data");
                }
            }
        });
    }
//    public abstract void getFromFirestore(String document_id);
//    public void getFromFirestore()
//    {
//        String collection_name = this.getClass().getSimpleName();
//        String document_id = this.getID();
//        System.out.println(collection_name);
//        System.out.println(document_id);
//        DocumentReference docref  = db.collection(collection_name).document(document_id);
//        Task<DocumentSnapshot> t = docref.get();
//        DocumentSnapshot docsnapshot = t.getResult();
//        if(t.isSuccessful()) {
//            Map<String, Object> map = docsnapshot.getData();
//            if(docsnapshot.exists()) {
//                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    System.out.println("------------");
//                    System.out.println(entry.getKey());
//                    System.out.println(entry.getValue());
//                }
//            }
//            else{
//                System.out.println("Empty---------");
//            }
//        }
//        else {
//            System.out.println("Some problem getting-------");
//        }
//
//    }
}
