package appdev.obenita.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Obenita on 26/1/2018.
 */

public class OpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GenericDrugFinder.db";
    public static final String TABLE_NAME1 = "PRESCRIPTION";
    public static final String TABLE_NAME2 = "DOCTOR";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOCTOR_ID = "_id";
    public static final String COLUMN_DOCTOR_ID2 = "DOCTOR_ID2";
    public static final String COLUMN_MEDICINE_NAME = "MEDICINE_NAME";
    public static final String COLUMN_DOSAGE = "DOSAGE";
    public static final String COLUMN_OFTEN = "OFTEN";
    public static final String COLUMN_TAKE = "TAKE";
    public static final String COLUMN_DURATION = "DURATION";
    public static final String COLUMN_DATE = "DATE";

    public static final String COLUMN_DOCTOR_NAME = "DOCTOR_NAME";
    public static final String COLUMN_CONTACT_NUMBER = "CONTACT_NUMBER";

    public static final String KEY_ID = "_id";
    public static final String KEY_MEDICINE_NAME = "MEDICINE_NAME";
    public static final String KEY_BRAND_NAME = "BRAND_NAME";
    public static final String KEY_DRUG_PURPOSE = "DRUG_PURPOSE";
    private static final String SQLITE_TABLE1 = "DRUGS";
    public static final String TAG = "OpenHelper";

    private static final String DATABASE_CREATE1 =
            "CREATE TABLE if not exists " + SQLITE_TABLE1 + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_MEDICINE_NAME + "," +
                    KEY_BRAND_NAME + "," +
                    KEY_DRUG_PURPOSE +");";

    public OpenHelper(Context context){
        super(context, DATABASE_NAME, null  , 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String createTableDoctor = "create table DOCTOR ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "DOCTOR_NAME TEXT, CONTACT_NUMBER TEXT)";
        db.execSQL(createTableDoctor);*/

        String createTableMedicine = "create table PRESCRIPTION ( _id INTEGER PRIMARY KEY AUTOINCREMENT,MEDICINE_NAME TEXT , " +
                "DOSAGE TEXT, OFTEN TEXT, TAKE TEXT, DURATION TEXT, DATE TEXT);";
        db.execSQL(createTableMedicine);
        //db.execSQL("PRAGMA foreign_keys=ON");
        db.execSQL(DATABASE_CREATE1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String upgrade = "DROP IF TABLE EXISTS '"+ TABLE_NAME1 + "'";
        db.execSQL(upgrade);
        String upgrade1 = "DROP IF TABLE EXISTS '"+ SQLITE_TABLE1 + "'";
        db.execSQL(upgrade1);
    }

    public long createMedicine(String gName, String bName, String purpose){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(KEY_MEDICINE_NAME, gName);
        content.put(KEY_BRAND_NAME, bName);
        content.put(KEY_DRUG_PURPOSE, purpose);

        return db.insert(SQLITE_TABLE1, null, content);
    }

    public boolean deleteAllMedicine() {
        SQLiteDatabase db = this.getWritableDatabase();
        int doneDelete = 0;

        doneDelete = db.delete(SQLITE_TABLE1, null , null);
        Log.w(TAG, Integer.toString(doneDelete));

        return doneDelete > 0;
    }

    public Cursor getMedicineByName(String inputText) throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE1, new String[] {KEY_ID,
                            KEY_MEDICINE_NAME, KEY_BRAND_NAME,KEY_DRUG_PURPOSE},
                    null, null, null, null, KEY_MEDICINE_NAME + " ASC");
        }
        else {
            mCursor = db.query(true, SQLITE_TABLE1, new String[] {KEY_ID,
                            KEY_MEDICINE_NAME, KEY_BRAND_NAME, KEY_DRUG_PURPOSE},
                    KEY_MEDICINE_NAME + " like '%" + inputText + "%'", null,
                    null, null, KEY_MEDICINE_NAME + " ASC", null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor getAllMedicine() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.query(SQLITE_TABLE1, new String[] {KEY_ID,
                        KEY_MEDICINE_NAME, KEY_BRAND_NAME, KEY_DRUG_PURPOSE},
                null, null, null, null, KEY_MEDICINE_NAME + " ASC");

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean addPrescription(MedicineModel medicineModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();

        content.put(COLUMN_MEDICINE_NAME, medicineModel.getMedicine());
        content.put(COLUMN_TAKE, medicineModel.getTake());
        content.put(COLUMN_DOSAGE, medicineModel.getDosage());
        content.put(COLUMN_DURATION, medicineModel.getDuration());
        content.put(COLUMN_OFTEN, medicineModel.getOften());
        content.put(COLUMN_DATE, medicineModel.getDate());

        long res = db.insert(TABLE_NAME1, null, content);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addDoctorDetails(MedicineModel medicineModel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues content = new ContentValues();

        content.put(COLUMN_DOCTOR_NAME, medicineModel.getDoctorName());
        content.put(COLUMN_CONTACT_NUMBER, medicineModel.getNumber());

        long res = db.insert(TABLE_NAME2, null, content);

        if(res == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateData(MedicineModel medicineModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_MEDICINE_NAME, medicineModel.getMedicine());
        contentValues.put(COLUMN_TAKE, medicineModel.getTake());
        contentValues.put(COLUMN_DOSAGE, medicineModel.getDosage());
        contentValues.put(COLUMN_DURATION, medicineModel.getDuration());
        contentValues.put(COLUMN_OFTEN, medicineModel.getOften());

        return db.update(TABLE_NAME1, contentValues, COLUMN_ID + " = ?", new String[]{medicineModel.getId()}) != 0;
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM PRESCRIPTION", null);
        return data;
    }

    public Cursor getDoctorData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME2, null);
        return data;
    }

    /*public ArrayList<MedicineModel> getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME1, null, null, null, null, null, null);

        ArrayList<MedicineModel> medicine = new ArrayList<MedicineModel>();
        MedicineModel medicineModel;
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                medicineModel = new MedicineModel();
                medicineModel.setId(cursor.getString(0));
                medicineModel.setMedicine(cursor.getString(1));
                medicineModel.setDosage(cursor.getString(2));
                medicineModel.setOften(cursor.getString(3));
                medicineModel.setTake(cursor.getString(4));
                medicineModel.setDuration(cursor.getString(5));

                medicine.add(medicineModel);
            }
        }
        cursor.close();
        return medicine;
    }*/

    public void insertMedicine(){
        createMedicine("Levothyroxine", "Synthroid","Thyroid Hormone");
        createMedicine("Hydrocodone/APAP","Vicodin", "Pain Relief");
        createMedicine("Amoxicillin","Amoxil","Anti-Biotic");
        createMedicine("Lisinopril","Prinivil","A.C.E. Inhibitor");
        createMedicine("Esomeprazole","Nexium","G.E.R.D.");
        createMedicine("Atorvastatin","Lipitor","Cholesterol");
        createMedicine("Simvastatin","Zocor","Cholesterol");
        createMedicine("Clopidogrel","Plavix","Anti-Platelet");
        createMedicine("Montelukast","Singulair","Asthma");
        createMedicine("Rosuvastatin","Crestor","Cholesterol");
        createMedicine("Metoprolol","Lopressor","Beta Blocker");
        createMedicine("Escitalopram","Lexapro","Anti-Depressant");
        createMedicine("Azithromycin","Zithromax","Anti-Biotic");
        createMedicine("Albuterol","ProAir","Asthma Inhaler");
        createMedicine("Hydrochlorothiazide","HCTZ","Diuretic");
        createMedicine("Metformin","Glucophage","Anti-Diabetic");
        createMedicine("Sertraline","Zoloft","Anti-Depressant");
        createMedicine("Ibuprofen","Advil","N.S.A.I.D.");
        createMedicine("Zolpidem","Ambien","Insomnia");
        createMedicine("Furosemide","Lasix","Diuretic");
        createMedicine("Omeprazole","Prilosec","G.E.R.D.");
        createMedicine("Trazodone","Desyrel","Anti-Depressant");
        createMedicine("Valsartan","Diovan","A.2.R.B.");
        createMedicine("Tramadol","Ultram","Pain Relief");
        createMedicine("Duloxetine","Cymbalta","Anti-Depressant");
        createMedicine("Warfarin","Coumadin","Blood Thinner");
        createMedicine("Amlodipine","Norvasc","Calc. Chnl. Blkr.");
        createMedicine("Oxycodone/APAP","Percocet","Pain Relief");
        createMedicine("Quetiapine","Seroquel","Anti-Psychotic");
        createMedicine("Promethazine","Phenergan","Anti-Histamine");
        createMedicine("Fluticasone","Flonase","Allergies ");
        createMedicine("Alprazolam","Xanax","Anti-Anxiety");
        createMedicine("Clonazepam","Klonopin","Anti-Anxiety");
        createMedicine("Benazepril","Lotensin","ACE Inhibitor");
        createMedicine("Meloxicam","Mobic","NSAID (Arthritis)");
        createMedicine("Citalopram","Celexa","Anti-Depressant");
        createMedicine("Cephalexin","Keflex","Anti-Biotic");
        createMedicine("Tiotropium","Spiriva","C.O.P.D.");
        createMedicine("Gabapentin","Neurontin","Anti-Epileptic");
        createMedicine("Aripiprazole","Abilify","Antipsychotic");
        createMedicine("Potassium","K-Tab","Electrolyte");
        createMedicine("Cyclobenzaprine","Flexeril","Muscle Relaxer");
        createMedicine("Methylprednisolone","Medrol","Corticosteroid");
        createMedicine("Methylphenidate","Concerta","A.D.H.D.");
        createMedicine("Loratadine","Claritin","Allergies");
        createMedicine("Carvedilol","Coreg","C.H.F.");
        createMedicine("Carisoprodol","Soma","Muscle Relaxer");
        createMedicine("Digoxin","Lanoxin","C.H.F.");
        createMedicine("Memantine","Namenda","Alzheimers");
        createMedicine("Atenolol","Tenormin","Beta Blocker");
        createMedicine("Diazepam","Valium","Anti-Anxiety");
        createMedicine("Oxycodone  ","OxyContin","Pain Relief");
        createMedicine("Risedronate","Actonel","Osteoporosis");
        createMedicine("Folic Acid","Folvite","Supplement");
        createMedicine("Losartan + HCTZ","Hyzaar","Hypertension");
        createMedicine("Prednisone","Deltasone","Anti-Inflammatory");
        createMedicine("Prednisolone","Omnipred","Anti-Inflammatory");
        createMedicine("Alendronate","Fosamax","Osteoporosis    ");
        createMedicine("Pantoprazole","Protonix","G.E.R.D.");
        createMedicine("Tamsulosin","Flomax","Freq. Urination");
        createMedicine("Triamterene + HCTZ","Dyazide","Diuretic Combo");
        createMedicine("Paroxetine","Paxil","Anti-Depressant");
        createMedicine("Buprenorphine + Naloxone","Suboxone","Opiate Addiction");
        createMedicine("Enalapril","Vasotec","A.C.E. Inhibitor");
        createMedicine("Lovastatin","Mevacor","Cholesterol");
        createMedicine("Pioglitazone","Actos","Diabetes");
        createMedicine("Pravastatin","Pravachol","Cholesterol");
        createMedicine("Fluoxetine","Prozac","Anti-Depressant");
        createMedicine("Insulin Detemir","Levemir","Long-Acting Insulin");
        createMedicine("Fluconazole","Diflucan","Anti-Fungal");
        createMedicine("Levofloxacin","Levaquin","Anti-Biotic");
        createMedicine("Rivaroxaban","Xarelto","Blood Thinner");
        createMedicine("Celecoxib","Celebrex","N.S.A.I.D.");
        createMedicine("Codeine / APAP","Tylenol","Pain Relief");
        createMedicine("Mometasone","Nasonex","Allergies");
        createMedicine("Ciprofloxacin","Cipro","Anti-Biotic");
        createMedicine("Pregabalin","Lyrica","Anti-Convulsant");
        createMedicine("Insulin Aspart","Novolog","Rapid-Acting Insulin");
        createMedicine("Venlafaxine","Effexor","Anti-Depressant");
        createMedicine("Lorazepam","Ativan","Anti-Anxiety");
        createMedicine("Ezetimibe","Zetia","Cholesterol");
        createMedicine("Estrogen","Premarin","Menopause");
        createMedicine("Allopurinol","Zyloprim","Anti-gout");
        createMedicine("Penicillin","Pen VK","Anti-Biotic");
        createMedicine("Sitagliptin","Januvia","Diabetes");
        createMedicine("Amitriptyline","Elavil","Anti-Depressant");
        createMedicine("Clonidine","Catapres","Hypertension");
        createMedicine("Latanoprost","Xalatan","Glaucoma");
        createMedicine("Lisdexamfetamine","Vyvanse","A.D.H.D.");
        createMedicine("Fluticasone + Salmeterol","Advair","Asthma/COPD");
        createMedicine("Budesonide + Formoterol","Symbicort","Asthma/COPD");
        createMedicine("Dexlansoprazole","Dexilant","G.E.R.D.");
        createMedicine("Glyburide","Diabeta","Diabetes");
        createMedicine("Olanzapine","Zyprexa","Anti-Psychotic");
        createMedicine("Tolterodine","Detrol","Overactive Bladder");
        createMedicine("Ranitidine","Zantac","G.E.R.D.");
        createMedicine("Famotidine","Pepcid","G.E.R.D.");
        createMedicine("Diltiazem ","Cardizem","Hypertension");
        createMedicine("Insulin Glargine","Lantus","Long-Acting Insulin");
        createMedicine("Lisinopril + HCTZ","Prinizide","Hypertension");
        createMedicine("Bupropion","Wellbutrin","Antidepressant");
        createMedicine("Cetirizine","Zyrtec","Allergies");
        createMedicine("Topiramate","Topamax","Antiepileptic");
        createMedicine("Valacyclovir","Valtrex","Herpes Mgmt.");
        createMedicine("Eszopiclone","Lunesta","Sleep Aid");
        createMedicine("Acyclovir","Zovirax","Herpes Mgmt.");
        createMedicine("Cefdinir","Omnicef","Antibiotic");
        createMedicine("Clindamycin","Cleocin","Antibiotic");
        createMedicine("Levetiracetam","Keppra","Anti-Seizure");
        createMedicine("Gemfibrozil","Lopid","Cholesterol");
        createMedicine("Guaifenesin","Robitussin","Expectorant");
        createMedicine("Glipizide","Glucotrol","Diabetes(II)");
        createMedicine("Irbesartan","Avapro","A.2.R.B.");
        createMedicine("Metoclopramide","Reglan","G.E.R.D.");
        createMedicine("Losartan","Cozaar","Hypertension");
        createMedicine("Meclizine","Dramamine","Antiemetic");
        createMedicine("Metronidazole","Flagyl","Antibiotic");
        createMedicine("Vitamin D","Caltrate","Supplement");
        createMedicine("Testosterone","AndroGel","Low T");
        createMedicine("Ropinirole","Requip","Parkinson's");
        createMedicine("Risperidone","Risperdal","Antipsychotic");
        createMedicine("Olopatadine","Patanol","Antihistamine");
        createMedicine("Donepezil","Aricept","Dementia");
        createMedicine("Dexmethylphenidate","Focalin","ADHD");
        createMedicine("Enoxaparin","Lovenox","Anti-coagulant");
        createMedicine("Fentanyl","Duragesic","Narcotic Analgesic");
        createMedicine("Dicyclomine","Bentyl","I.B.S.");
        createMedicine("Levalbuterol","Xopenex","Bronchospasm");
        createMedicine("Atomoxetine","Strattera","A.D.H.D.");
        createMedicine("Ramipril","Altace","Hypertension");
        createMedicine("Temazepam","Restoril","Sleep Aid");
        createMedicine("Phentermine","Adipex","Weight Loss");
        createMedicine("Quinapril","Accupril","ACE Inhibitor");
        createMedicine("Sildenafil","Viagra","Impotence");
        createMedicine("Ondansetron","Zofran","Antiemetic");
        createMedicine("Oseltamivir","Tamiflu","Antiviral (Flu)");
        createMedicine("Methotrexate","Rheumatrex","Rheum. arthritis");
        createMedicine("Dabigatran","Pradaxa","Anticoagulant");
        createMedicine("Budesonide","Uceris","Ulcerative colitis");
        createMedicine("Doxazosin","Cardura","Freq. Urination");
        createMedicine("Desvenlafaxine","Pristiq","Anti-depressant");
        createMedicine("Insulin Lispro","Humalog","Rapid-Acting Insulin");
        createMedicine("Clarithromycin","Biaxin","Antibiotic");
        createMedicine("Buspirone","Buspar","Anti-anxiety");
        createMedicine("Finasteride","Proscar","B.P.H.");
        createMedicine("Ketoconazole","Nizoral","Antifungal");
        createMedicine("Solifenacin","VESIcare","Overactive Bladder");
        createMedicine("Methadone","Dolophine","Anti-addictive");
        createMedicine("Minocycline","Minocin","Antibiotic");
        createMedicine("Phenazopyridine","Pyridium","U.T.I.");
        createMedicine("Spironolactone","Aldactone","Diuretic");
        createMedicine("Vardenafil","Levitra","Impotence");
        createMedicine("Clobetasol","Clovate","Corticosteroid");
        createMedicine("Benzonatate","Tessalon","Antitussive");
        createMedicine("Divalproex","Depakote","Antiepileptic");
        createMedicine("Dutasteride","Avodart","B.P.H.");
        createMedicine("Febuxostat","Uloric","Gout");
        createMedicine("Lamotrigine","Lamictal","Antiepileptic");
        createMedicine("Nortriptyline","Pamelor","Antidepressant");
        createMedicine("Glimepiride","Amaryl","Anti-Diabetes");
        createMedicine("Rabeprazole","Aciphex","G.E.R.D.");
        createMedicine("Etanercept","Enbrel","Anti-Arthritis");
        createMedicine("Nebivolol","Bystolic","Beta Blocker");
        createMedicine("Nabumetone","Relafen","N.S.A.I.D");
        createMedicine("Nifedipine","Procardia","Calc. Chan. Blocker");
        createMedicine("Nitrofurantoin","Macrobid","Antibiotic");
        createMedicine("Nitroglycerine","NitroStat","Angina");
        createMedicine("Oxybutynin","Ditropan","Incontinence");
        createMedicine("Tadalifil","Cialis","Impotence");
        createMedicine("Triamcinolone","Kenalog","Corticosteroid");
        createMedicine("Rivastigmine","Exelon","Anti-Dimentia");
        createMedicine("Lansoprazole","Prevacid","G.E.R.D.");
        createMedicine("Cefuroxime","Ceftin","Antibiotic");
        createMedicine("Methocarbamol","Robaxin","Muscle Relaxer");
        createMedicine("Travoprost","Travatan","Ocular Hypertension");
        createMedicine("Lurasidone","Latuda","Antipsychotic");
        createMedicine("Terazosin","Hytrin","B.P.H.");
        createMedicine("Sumatriptan","Imitrex","Migraine");
        createMedicine("Raloxifene","Evista","Osteoporosis");
        createMedicine("Mirtazepine","Remeron","Antidepressant");
        createMedicine("Adalimumab","Humira","Anti-inflammatory");
        createMedicine("Benztropine","Cogentin","Parkinson's");
        createMedicine("Baclofen","Gablofen","Muscle Relaxer");
        createMedicine("Hydralazine","Apresoline","Hypertension");
        createMedicine("Mupirocin","Bactroban","Antibacterial");
        createMedicine("Propranolol","Inderal","Hypertension");
        createMedicine("Nystatin","Mycostatin","Candidiasis");
        createMedicine("Verapamil","Verelan","Calc. Chan. Blocker");
        createMedicine("Estradiol","Estrace","Menopause");
        createMedicine("Phenytoin","Dilantin","Anticonvulsant");
        createMedicine("Fenofibrate","Tricor","Cholesterol");
        createMedicine("Liraglutide","Victoza","Anti-Diabetic");
        createMedicine("Ticagrelor","Brilinta","Heart Disease");
        createMedicine("Diclofenac","Voltaren","NSAID Gel");
        createMedicine("Saxagliptin","Onglyza","Anti-Diabetic");
        createMedicine("Naproxen","Aleve","N.S.A.I.D.");
        createMedicine("Tizanidine","Zanaflex","Muscle Relaxer");
        createMedicine("Amphetamine/Dextro-amphetamine","Adderall","ADHD / Narcolepsy");
        createMedicine("Amoxicillin + Clavulanate","Augmentin","Bacteria Infection");
        createMedicine("Ezetimibe + Simvastatin","Vytorin","Cholesterol");
    }

}
