package gripe._90.arseng.data;

import java.io.IOException;

import com.hollingsworth.arsnouveau.common.datagen.PatchouliProvider;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.ApparatusPage;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.CraftingPage;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.IPatchouliPage;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.PatchouliBuilder;
import com.hollingsworth.arsnouveau.common.datagen.patchouli.TextPage;

import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import gripe._90.arseng.definition.ArsEngBlocks;
import gripe._90.arseng.definition.ArsEngItems;

@SuppressWarnings("deprecation")
public class DocumentationProvider extends PatchouliProvider {
    public DocumentationProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public PatchouliBuilder buildBasicItem(ItemLike item, ResourceLocation category, IPatchouliPage recipePage) {
        var builder = new PatchouliBuilder(category, item.asItem().getDescriptionId())
                .withIcon(item.asItem())
                .withPage(new TextPage(
                        "arseng.page." + Registry.ITEM.getKey(item.asItem()).getPath()));

        if (recipePage != null) {
            builder.withPage(recipePage);
        }

        return builder;
    }

    @Override
    public String getLangPath(String name) {
        return "arseng.page." + name;
    }

    @Override
    public void addEntries() {
        var sourceAcceptorBuilder = buildBasicItem(
                ArsEngBlocks.SOURCE_ACCEPTOR, AUTOMATION, new ApparatusPage(ArsEngBlocks.SOURCE_ACCEPTOR));
        sourceAcceptorBuilder.withPage(new TextPage(getLangPath("source_acceptor_description")));
        sourceAcceptorBuilder.withPage(new CraftingPage(ArsEngItems.SOURCE_ACCEPTOR_PART));
        addPage(new PatchouliPage(
                sourceAcceptorBuilder,
                getPath(AUTOMATION, Registry.ITEM.getKey(ArsEngBlocks.SOURCE_ACCEPTOR.asItem()))));

        var cellsBuilder = buildBasicItem(
                ArsEngItems.SOURCE_CELL_HOUSING, AUTOMATION, new CraftingPage(ArsEngItems.SOURCE_CELL_HOUSING));
        ArsEngItems.getCells().forEach(cell -> cellsBuilder.withPage(new CraftingPage(cell)));
        addPage(new PatchouliPage(cellsBuilder, getPath(AUTOMATION, "me_cells")));
    }

    @Override
    public void run(CachedOutput cache) throws IOException {
        addEntries();

        for (var page : pages) {
            DataProvider.saveStable(cache, page.build(), page.path());
        }
    }
}