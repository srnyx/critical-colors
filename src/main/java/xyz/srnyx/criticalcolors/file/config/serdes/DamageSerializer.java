package xyz.srnyx.criticalcolors.file.config.serdes;

import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import org.jetbrains.annotations.NotNull;
import xyz.srnyx.criticalcolors.file.config.Damage;

import java.util.Objects;


public class DamageSerializer implements ObjectSerializer<Damage> {
    @Override
    public boolean supports(@NotNull Class<?> type) {
        return Damage.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NotNull Damage object, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        data.setValue(Objects.requireNonNullElse(object.amount(), "kill"));
    }

    @Override @NotNull
    public Damage deserialize(@NotNull DeserializationData data, @NotNull GenericsDeclaration generics) {
        try {
            return new Damage(data.getValue(Double.class));
        } catch (final OkaeriException e) {
            return new Damage();
        }
    }
}
