package eu.nanooq.otkd.helpers;

import java.util.ArrayList;

import eu.nanooq.otkd.models.API.Member;
import eu.nanooq.otkd.models.API.Section;
import eu.nanooq.otkd.models.UI.SectionItem;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;

/**
 *
 * Created by rd on 29/07/2017.
 */

public class CombineHelper {

    void help() {
        Flowable<ArrayList<Section>> sections = Flowable.just( new ArrayList<Section>());
        Flowable<ArrayList<Member>>  members  = Flowable.just(new ArrayList<Member>());
        Flowable<ArrayList<SectionItem>> items = Flowable.combineLatest(sections, members, new BiFunction<ArrayList<Section>, ArrayList<Member>, ArrayList<SectionItem>>() {
            @Override
            public ArrayList<SectionItem> apply(ArrayList<Section> sections, ArrayList<Member> members) throws Exception {

                ArrayList<SectionItem> sectionItems = new ArrayList<>();

                for (Section section: sections) {
                    SectionItem item = new SectionItem();

                }

                return new ArrayList<SectionItem>();
            }
        });
    }
}
