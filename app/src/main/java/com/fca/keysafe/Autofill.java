package com.fca.keysafe;

import android.app.assist.AssistStructure;
import android.os.Build;
import android.os.CancellationSignal;
import android.service.autofill.AutofillService;
import android.service.autofill.Dataset;
import android.service.autofill.FillCallback;
import android.service.autofill.FillContext;
import android.service.autofill.FillRequest;
import android.service.autofill.FillResponse;
import android.service.autofill.SaveCallback;
import android.service.autofill.SaveRequest;
import android.util.Log;
import android.view.autofill.AutofillValue;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Autofill extends AutofillService {

    FillResponse.Builder fillResponseBuilder;
    ArrayList<Account> accounts;

    @Override
    public void onFillRequest(@NonNull FillRequest request, @NonNull CancellationSignal cancellationSignal, @NonNull FillCallback callback) {
        List<FillContext> context = request.getFillContexts();
        AssistStructure structure = context.get(context.size() - 1).getStructure();

        fillResponseBuilder = new FillResponse.Builder();

        accounts = new Helpers().readAccounts(getApplicationContext());

        boolean success = traverseStructure(structure);

        Log.d(null, "Found: " + success);

        if(success)
            callback.onSuccess(fillResponseBuilder.build());
    }

    @Override
    public void onSaveRequest(@NonNull SaveRequest request, @NonNull SaveCallback callback) {

    }

    public boolean traverseStructure(AssistStructure structure){
        int nodes = structure.getWindowNodeCount();

        boolean returnedResult = false;

        for(int i = 0; i < nodes; i++) {
            AssistStructure.WindowNode windowNode = structure.getWindowNodeAt(i);
            AssistStructure.ViewNode viewNode = windowNode.getRootViewNode();
            if(traverseNode(viewNode))
                returnedResult = true;
        }

        return returnedResult;
    }

    public boolean traverseNode(AssistStructure.ViewNode viewNode) {
        boolean returnedResult = false;

        if(viewNode.getAutofillHints() != null && viewNode.getAutofillHints().length > 0) {
            // the client app provided autofill hints
            if(contains(viewNode.getAutofillHints(), "username") || contains(viewNode.getAutofillHints(), "email")) {
                populateResponseBuilder("username", viewNode);
                returnedResult = true;
            } else if(contains(viewNode.getAutofillHints(), "password")) {
                populateResponseBuilder("password", viewNode);
                returnedResult = true;
            }
        } else if(viewNode.getHint() != null && viewNode.getHint().length() > 0) {
            // the client app haven't provided autofill hints
            if (contains(viewNode.getHint().split(" "), "username") || contains(viewNode.getHint().split(" "), "email")) {
                populateResponseBuilder("username", viewNode);
                returnedResult = true;
            } else if (contains(viewNode.getHint().split(" "), "password")) {
                populateResponseBuilder("password", viewNode);
                returnedResult = true;
            }
        }

        for(int i = 0; i < viewNode.getChildCount(); i++) {
            AssistStructure.ViewNode childNode = viewNode.getChildAt(i);
            if(traverseNode(childNode))
                returnedResult = true;
        }

        return returnedResult;
    }

    public void populateResponseBuilder(String query, AssistStructure.ViewNode viewNode) {

        for(Account account : accounts) {

            RemoteViews presentation = new RemoteViews(getPackageName(), android.R.layout.simple_list_item_1);
            String value;

            if (query.equals("username")) // is an username field
                value = account.getUsername();
            else
                value = account.getPassword();

            presentation.setTextViewText(android.R.id.text1, value);

            Dataset.Builder datasetBuilder = new Dataset.Builder();
            datasetBuilder.setValue(viewNode.getAutofillId(), AutofillValue.forText(value), presentation);

            fillResponseBuilder.addDataset(datasetBuilder.build());
        }
    }

    public boolean contains(String[] arr, String query) {
        for(String s : arr) {
            if(s.toLowerCase().equals(query))
                return true;
        }
        return false;
    }
}
