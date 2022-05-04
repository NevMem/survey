import { useParams } from "react-router-dom";
import SpacedColumn from "../../app/layout/SpacedColumn";
import PageWrapper from "../../app/page/PageWrapper";
import Text from "../../components/text/Text";
import useAsyncRequest, { isOk, RequestError } from "../../utils/useAsyncUtils";
import backendApi from '../../api/backendApiServiceSingleton';
import { Media, MediaGallery } from "../../data/exported";
import OutlinedCard from "../../app/card/OutlinedCard";
import SpaceAroundRow from "../../app/layout/SpaceAroundRow";
import Loader from "../../components/loader/Loader";
import CardError from "../../app/card/CardError";
import SpaceBetweenRow from "../../app/layout/SpaceBetweenRow";
import TextButton from "../../components/button/TextButton";
import Input from "../../components/input/Input";
import { useState } from "react";
import GeneralButton from "../../components/button/GeneralButton";
import SpacedRow from "../../app/layout/SpacedRow";
import { useNotification } from "../../app/notification/NotificationProvider";
import useNavigator from "../navigation";

const GalleryGrid = (props: { gallery: MediaGallery }) => {
    const { gallery } = props;

    const openMedia = (media: Media) => {
        window.open(media.url, '_blank')?.focus();
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={8}>
                {gallery.gallery.map((media, index) => {
                    return (
                        <SpaceBetweenRow key={index}>
                            <Text>{media.filename}</Text>
                            <TextButton onClick={() => openMedia(media)}>Просмотреть</TextButton>
                        </SpaceBetweenRow>
                    );
                })}
            </SpacedColumn>
        </OutlinedCard>
    );
};

const GoToGalleryContent = () => {
    const [galleryId, setGalleryId] = useState('');

    const notification = useNotification();
    const navigator = useNavigator();

    const gotoGallery = () => {
        const id = parseInt(galleryId);

        if (isNaN(id)) {
            notification('Неправильный идентификатор', 'Идентификатором галереи должно быть число', 'error');
        } else {
            navigator.gallery(id);
        }
    };

    return (
        <OutlinedCard>
            <SpacedColumn rowGap={16}>
                <Text large>Выберите идентификатор галереи</Text>
                <SpacedRow columnGap={12}>
                    <Input value={galleryId} onChange={event => setGalleryId(parseInt(event.target.value) + "")} />
                    <GeneralButton onClick={() => gotoGallery()}>Поиск</GeneralButton>
                </SpacedRow>
            </SpacedColumn>
        </OutlinedCard>
    );
};

const LoadGalleryPageContent = (props: { id: number }) => {
    const request = useAsyncRequest(controller => backendApi.gallery(controller, props.id));

    if (isOk(request)) {
        return (
            <GalleryGrid gallery={request.result} />
        );
    } else if (request instanceof RequestError) {
        return (
            <CardError>
                {request.message}
            </CardError>
        );
    }

    return (
        <OutlinedCard>
            <SpaceAroundRow>
                <Loader />
            </SpaceAroundRow>
        </OutlinedCard>
    );
};

const GalleryPageContent = () => {
    const { id } = useParams();

    if (id === undefined) {
        return (
            <GoToGalleryContent />
        );
    } else {
        return (
            <LoadGalleryPageContent id={parseInt(id)} />
        );
    }
};

const GalleryPage = () => {
    return (
        <PageWrapper>
            <SpacedColumn rowGap={24}>
                <Text header>Галерея</Text>
                <GalleryPageContent />
            </SpacedColumn>
        </PageWrapper>
    );
}

export default GalleryPage;
